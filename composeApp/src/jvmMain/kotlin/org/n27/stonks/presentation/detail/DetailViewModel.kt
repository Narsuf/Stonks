package org.n27.stonks.presentation.detail

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event.GoBack
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.detail.entities.DetailInteraction
import org.n27.stonks.presentation.detail.entities.DetailInteraction.BackClicked
import org.n27.stonks.presentation.detail.entities.DetailInteraction.Retry
import org.n27.stonks.presentation.detail.entities.DetailState
import org.n27.stonks.presentation.detail.entities.DetailState.*
import org.n27.stonks.presentation.detail.mapping.toDetailContent

class DetailViewModel(
    private val params: DetailParams,
    private val eventBus: EventBus,
    private val repository: Repository,
) : ViewModel() {
    private val state = MutableStateFlow<DetailState>(Idle)
    internal val viewState = state.asStateFlow()

    init { requestStock() }

    internal fun handleInteraction(action: DetailInteraction) = when(action) {
        BackClicked -> viewModelScope.launch { eventBus.emit(GoBack()) }
        Retry -> requestStock()
    }

    private fun requestStock() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStock(params.symbol)
                .fold(
                    onSuccess = { it.toDetailContent() },
                    onFailure = { Error }
                )

            state.emit(newState)
        }
    }
}

data class DetailParams(
    val symbol: String,
    val expectedEpsGrowth: Double? = null
)
