package org.n27.stonks.presentation.home

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.home.Home
import org.n27.stonks.domain.home.StockInfo
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToDetail
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch.Origin
import org.n27.stonks.presentation.common.broadcast.Event.ShowErrorNotification
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.home.entities.HomeEvent
import org.n27.stonks.presentation.home.entities.HomeEvent.ShowBottomSheet
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.*
import org.n27.stonks.presentation.home.entities.HomeState
import org.n27.stonks.presentation.home.entities.HomeState.*
import org.n27.stonks.presentation.home.mapping.toContent
import org.n27.stonks.presentation.home.mapping.toPresentationEntity

class HomeViewModel(
    private val eventBus: EventBus,
    private val repository: Repository,
) : ViewModel() {
    private val state = MutableStateFlow<HomeState>(Idle)
    internal val viewState = state.asStateFlow()

    private val event = Channel<HomeEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    private lateinit var currentHome: Home

    init { requestWatchlist() }

    internal fun handleInteraction(action: HomeInteraction) = when(action) {
        Retry -> Unit
        SearchClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch()) }
        AddClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch(Origin.WATCHLIST)) }
        is ItemClicked -> onItemClicked(action.index)
        is RemoveItemClicked -> onRemoveItemClicked(action.index)
        is EditItemClicked -> onEditItemClicked(action.index)
    }

    private fun requestWatchlist() {
        viewModelScope.launch {
            state.emit(Loading)
            repository.getWatchlist()
                .onSuccess { requestStocks(it.items) }
                .onFailure {
                    eventBus.emit(
                        ShowErrorNotification(
                            title = "Something went wrong when trying to read stonks.json"
                        )
                    )
                }
        }
    }

    private suspend fun requestStocks(stocks: List<StockInfo>) {
        repository.getStocks(stocks.map { it.symbol })
            .onSuccess {
                currentHome = it
                state.emit(it.toContent())
            }
            .onFailure { state.emit(Error) }
    }

    override fun onResult(result: String) {
        viewModelScope.launch {
            repository.addToWatchlist(result)
                .onSuccess { requestWatchlist() }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }

    private fun onItemClicked(index: Int) {
        viewModelScope.launch {
            val symbol = currentHome.items[index].symbol
            eventBus.emit(NavigateToDetail(symbol))
        }
    }

    private fun onRemoveItemClicked(index: Int) {
        viewModelScope.launch {
            val symbol = currentHome.items[index].symbol
            repository.removeFromWatchlist(symbol)
                .onSuccess {
                    val newItems = currentHome.items.toMutableList().apply { removeAt(index) }
                    currentHome = currentHome.copy(items = newItems)
                    state.updateIfType { c: Content -> c.copy(watchlist = currentHome.items.toPresentationEntity()) }
                }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }

    private fun onEditItemClicked(index: Int) {
        event.trySend(ShowBottomSheet(index))
    }
}
