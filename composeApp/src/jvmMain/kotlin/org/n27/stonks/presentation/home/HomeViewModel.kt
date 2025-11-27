package org.n27.stonks.presentation.home

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.common.Stocks
import org.n27.stonks.domain.watchlist.WatchlistUseCase
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
import org.n27.stonks.presentation.home.entities.HomeEvent.CloseBottomSheet
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
    private val useCase: WatchlistUseCase,
) : ViewModel() {
    private val state = MutableStateFlow<HomeState>(Idle)
    internal val viewState = state.asStateFlow()

    private val event = Channel<HomeEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    private lateinit var currentStocks: Stocks

    init { requestWatchlist(isInitialRequest = true) }

    override fun onResult(result: String) {
        viewModelScope.launch {
            useCase.addToWatchlist(result)
                .onSuccess { requestWatchlist(isInitialRequest = true) }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }

    internal fun handleInteraction(action: HomeInteraction) = when(action) {
        Retry -> requestWatchlist(isInitialRequest = true)
        SearchClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch()) }
        AddClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch(Origin.WATCHLIST)) }
        LoadNextPage -> requestMoreStocks()
        is ItemClicked -> onItemClicked(action.index)
        is RemoveItemClicked -> onRemoveItemClicked(action.index)
        is EditItemClicked -> onEditItemClicked(action.index)
        is ValueChanged -> onValueChanged(action.value)
        is ValueUpdated -> onValueUpdated(action.index, action.value)
    }

    private fun requestWatchlist(isInitialRequest: Boolean = false) {
        viewModelScope.launch {
            var from: Int? = 0

            if (isInitialRequest)
                state.emit(Loading)
            else
                from = currentStocks.nextPage

            useCase.getWatchlist(from)
                .onSuccess {
                    currentStocks = if (isInitialRequest)
                        it
                    else
                        currentStocks.copy(
                            nextPage = it.nextPage,
                            items = currentStocks.items + it.items
                        )

                    state.emit(currentStocks.toContent())
                }
                .onFailure {
                    if (isInitialRequest)
                        state.emit(Error)
                    else
                        eventBus.emit(ShowErrorNotification("Something went wrong."))
                }
        }
    }

    private fun requestMoreStocks() {
        viewModelScope.launch {
            state.updateIfType { c: Content -> c.copy(isPageLoading = true) }
            requestWatchlist()
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
            useCase.removeFromWatchlist(symbol)
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
            c.copy(input = item.expectedEpsGrowth?.toFormattedBigDecimal() ?: BigDecimal.ZERO)
        }
        event.trySend(ShowBottomSheet(index))
    }

    private fun onValueChanged(value: BigDecimal) {
        state.updateIfType { c: Content -> c.copy(input = value) }
    }

    private fun onValueUpdated(index: Int, value: BigDecimal) {
        viewModelScope.launch {
            val item = currentStocks.items[index]
            event.send(CloseBottomSheet)
            useCase.editWatchlistItem(item.symbol, value.toDouble())
                .onSuccess {
                    val newItems = currentStocks.items.toMutableList()
                    newItems[index] = newItems[index].copy(expectedEpsGrowth = value.toDouble())
                    currentStocks = currentStocks.copy(items = newItems)

                    state.updateIfType { c: Content ->
                        c.copy(watchlist = currentStocks.items.toPresentationEntity())
                    }
                }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }
}
