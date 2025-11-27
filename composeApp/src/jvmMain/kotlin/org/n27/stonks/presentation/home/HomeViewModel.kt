package org.n27.stonks.presentation.home

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.models.common.Stocks
import org.n27.stonks.domain.models.watchlist.StockInfo
import org.n27.stonks.domain.models.watchlist.Watchlist
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
import java.math.BigDecimal

class HomeViewModel(
    private val eventBus: EventBus,
    private val repository: Repository,
) : ViewModel() {
    private val state = MutableStateFlow<HomeState>(Idle)
    internal val viewState = state.asStateFlow()

    private val event = Channel<HomeEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    private lateinit var currentWatchlist: Watchlist
    private lateinit var currentStocks: Stocks
    private var currentPage = 0
    private val pageSize = 11

    init { requestWatchlist(isInitialRequest = true) }

    override fun onResult(result: String) {
        viewModelScope.launch {
            repository.addToWatchlist(result)
                .onSuccess {
                    currentPage = 0
                    requestWatchlist(isInitialRequest = true)
                }
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
            if (isInitialRequest)
                state.emit(Loading)
            else
                state.updateIfType { c: Content -> c.copy(isWatchlistLoading = true) }

            repository.getWatchlist()
                .onSuccess {
                    val symbols = it.items.drop(currentPage).take(pageSize)
                    currentWatchlist = it
                    requestStocks(symbols, isInitialRequest)
                }
                .onFailure {
                    eventBus.emit(
                        ShowErrorNotification(
                            title = "Something went wrong when trying to read stonks.json"
                        )
                    )
                }
        }
    }

    private fun requestMoreStocks() {
        viewModelScope.launch {
            state.updateIfType { c: Content -> c.copy(isPageLoading = true) }
            val symbols = currentWatchlist.items.drop(currentPage).take(pageSize)
            requestStocks(symbols)
        }
    }

    private suspend fun requestStocks(stocks: List<StockInfo>, isInitialRequest: Boolean = false) {
        repository.getStocks(stocks.map { it.symbol })
            .onSuccess {
                currentPage += pageSize
                currentStocks = if (isInitialRequest)
                    it
                else
                    currentStocks.copy(items = currentStocks.items + it.items)

                state.emit(currentStocks.toContent(currentWatchlist))
            }
            .onFailure {
                if (isInitialRequest)
                    state.emit(Error)
                else
                    eventBus.emit(ShowErrorNotification("Something went wrong."))
            }
    }

    private fun onItemClicked(index: Int) {
        viewModelScope.launch {
            val item = currentWatchlist.items[index]
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
                    requestWatchlist()
                }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }

    private fun onEditItemClicked(index: Int) {
        val item = currentWatchlist.items[index]
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
            val item = currentWatchlist.items[index]
            event.send(CloseBottomSheet)
            repository.editWatchlistItem(item.symbol, value.toDouble())
                .onSuccess { requestWatchlist() }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }
}
