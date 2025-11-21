package org.n27.stonks.presentation.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.home.StockInfo
import org.n27.stonks.domain.home.Watchlist
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch.Origin
import org.n27.stonks.presentation.common.broadcast.Event.ShowErrorNotification
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.*
import org.n27.stonks.presentation.home.entities.HomeState
import org.n27.stonks.presentation.home.entities.HomeState.*
import org.n27.stonks.presentation.home.mapping.toContent

class HomeViewModel(
    private val eventBus: EventBus,
    private val repository: Repository,
) : ViewModel() {
    private val state = MutableStateFlow<HomeState>(Idle)
    internal val viewState = state.asStateFlow()

    init { requestWatchlist() }

    internal fun handleInteraction(action: HomeInteraction) = when(action) {
        Retry -> Unit
        SearchClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch()) }
        AddClicked -> viewModelScope.launch { eventBus.emit(Event.NavigateToSearch(Origin.WATCHLIST)) }
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
            .onSuccess { state.emit(it.toContent()) }
            .onFailure { state.emit(Error) }
    }

    override fun onResult(result: String) {
        viewModelScope.launch {
            repository.addToWatchlist(result)
                .onSuccess { requestWatchlist() }
                .onFailure { eventBus.emit(ShowErrorNotification("Something went wrong.")) }
        }
    }
}
