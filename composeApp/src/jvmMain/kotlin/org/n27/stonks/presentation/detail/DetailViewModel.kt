package org.n27.stonks.presentation.detail

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event.*
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.detail.entities.DetailInteraction
import org.n27.stonks.presentation.detail.entities.DetailInteraction.*
import org.n27.stonks.presentation.detail.entities.DetailState
import org.n27.stonks.presentation.detail.entities.DetailState.*
import org.n27.stonks.presentation.detail.mapping.toDetailContent
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.error_generic

class DetailViewModel(
    private val symbol: String,
    private val eventBus: EventBus,
    private val repository: Repository,
    dispatcher: CoroutineDispatcher,
) : ViewModel(dispatcher) {

    private val state = MutableStateFlow<DetailState>(Idle)
    internal val viewState = state.asStateFlow()

    private lateinit var internalStock: Stock

    init { requestStock() }

    internal fun handleInteraction(action: DetailInteraction) = when(action) {
        BackClicked -> viewModelScope.launch { eventBus.emit(GoBack()) }
        Retry -> requestStock()
        ToggleWatchlist -> onWatchlistToggled()
    }

    private fun requestStock() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStock(symbol)
                .fold(
                    onFailure = { Error },
                    onSuccess = {
                        internalStock = it
                        it.toDetailContent()
                    },
                )

            state.emit(newState)
        }
    }

    private fun onWatchlistToggled() {
        viewModelScope.launch {
            if (internalStock.isWatchlisted) {
                repository.removeFromWatchlist(symbol)
                    .onSuccess {
                        internalStock = internalStock.copy(isWatchlisted = false)
                        state.updateIfType { c: Content -> c.copy(isWatchlisted = false) }
                        eventBus.emit(WatchlistEvent.AssetRemoved)
                    }
                    .onFailure { eventBus.emit(ShowErrorNotification(Res.string.error_generic)) }
            } else {
                repository.addToWatchlist(symbol)
                    .onSuccess {
                        internalStock = internalStock.copy(isWatchlisted = true)
                        state.updateIfType { c: Content -> c.copy(isWatchlisted = true) }
                        eventBus.emit(WatchlistEvent.AssetAdded)
                    }
                    .onFailure { eventBus.emit(ShowErrorNotification(Res.string.error_generic)) }
            }
        }
    }
}
