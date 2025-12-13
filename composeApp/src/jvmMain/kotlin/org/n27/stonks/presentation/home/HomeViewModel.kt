package org.n27.stonks.presentation.home

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.n27.stonks.SYMBOL
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToDetail
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch.Origin
import org.n27.stonks.presentation.common.broadcast.Event.ShowErrorNotification
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.common.extensions.toFormattedBigDecimal
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.detail.DetailParams
import org.n27.stonks.presentation.home.entities.HomeEvent
import org.n27.stonks.presentation.home.entities.HomeEvent.ShowBottomSheet
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.*
import org.n27.stonks.presentation.home.entities.HomeState
import org.n27.stonks.presentation.home.entities.HomeState.*
import org.n27.stonks.presentation.home.mapping.toContent
import org.n27.stonks.presentation.home.mapping.toPresentationEntity
import java.math.BigDecimal

class HomeViewModel(
    private val eventBus: EventBus,
    private val repository: Repository,
) : ViewModel() {
    private val state = MutableStateFlow<HomeState>(Idle)
    internal val viewState = state.asStateFlow()

    private val event = Channel<HomeEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    private lateinit var currentStocks: Stocks

    init { requestWatchlist() }

    override fun onResult(result: Map<String, Any>) {
        viewModelScope.launch {
            val stock = result[SYMBOL] as String
            repository.addToWatchlist(stock)
                .onSuccess { requestWatchlist() }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }

    internal fun handleInteraction(action: HomeInteraction) = when(action) {
        Retry -> requestWatchlist()
        SearchClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch()) }
        AddClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch(Origin.WATCHLIST)) }
        LoadNextPage -> requestMoreStocks()
        is ItemClicked -> onItemClicked(action.index)
        is RemoveItemClicked -> onRemoveItemClicked(action.index)
        is EditItemClicked -> onEditItemClicked(action.index)
        is EpsGrowthValueChanged -> onEpsGrowthValueChanged(action.value)
        is ValuationFloorValueChanged -> onValuationFloorValueChanged(action.value)
        is ValuesUpdated -> onValuesUpdated(action.index, action.epsGrowth, action.valuationFloor)
    }

    private fun requestWatchlist() {
        viewModelScope.launch {
            state.emit(Loading)
            val newState = repository.getWatchlist()
                .onSuccess { currentStocks = it }
                .fold(
                    onSuccess = { currentStocks.toContent() },
                    onFailure = { Error },
                )

            state.emit(newState)
        }
    }

    private fun requestMoreStocks() {
        viewModelScope.launch {
            state.updateIfType { c: Content -> c.copy(isPageLoading = true) }
            repository.getWatchlist(currentStocks.nextPage)
                .onSuccess {
                    currentStocks = currentStocks.copy(
                        nextPage = it.nextPage,
                        items = currentStocks.items + it.items
                    )
                    state.emit(currentStocks.toContent())
                }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }

    private fun onItemClicked(index: Int) {
        viewModelScope.launch {
            val item = currentStocks.items[index]
            eventBus.emit(
                NavigateToDetail(
                    DetailParams(item.symbol, item.expectedEpsGrowth)
                )
            )
        }
    }

    private fun onRemoveItemClicked(index: Int) {
        viewModelScope.launch {
            val symbol = currentStocks.items[index].symbol
            repository.removeFromWatchlist(symbol)
                .onSuccess {
                    currentStocks = currentStocks.copy(
                        items = currentStocks.items.filterIndexed { i, _ -> i != index }
                    )

                    state.updateIfType { c: Content ->
                        c.copy(watchlist = currentStocks.items.toPresentationEntity())
                    }
                }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }

    private fun onEditItemClicked(index: Int) {
        val item = currentStocks.items[index]
        state.updateIfType { c: Content ->
            c.copy(
                bottomSheet = c.bottomSheet.copy(
                    epsGrowthInput = item.expectedEpsGrowth?.toFormattedBigDecimal() ?: BigDecimal.ZERO,
                    valuationFloorInput = item.valuationFloor?.toFormattedBigDecimal() ?: BigDecimal.ZERO,
                ),
            )
        }
        event.trySend(ShowBottomSheet(index))
    }

    private fun onEpsGrowthValueChanged(value: BigDecimal) {
        state.updateIfType { c: Content ->
            c.copy(
                bottomSheet = c.bottomSheet.copy(
                    epsGrowthInput = value,
                ),
            )
        }
    }

    private fun onValuationFloorValueChanged(value: BigDecimal) {
        state.updateIfType { c: Content ->
            c.copy(
                bottomSheet = c.bottomSheet.copy(
                    valuationFloorInput = value,
                ),
            )
        }
    }

    private fun onValuesUpdated(index: Int, epsGrowth: BigDecimal, valuationFloor: BigDecimal) {
        viewModelScope.launch {
            val item = currentStocks.items[index]
            event.send(HomeEvent.CloseBottomSheet)
            repository.editWatchlistItem(item.symbol, epsGrowth.toDouble(), valuationFloor.toDouble())
                .onSuccess { requestWatchlist() }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }
}
