package org.n27.stonks.presentation.app

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.n27.stonks.presentation.app.entities.AppEvent
import org.n27.stonks.presentation.app.entities.AppState
import org.n27.stonks.presentation.app.entities.AppState.Detail
import org.n27.stonks.presentation.app.entities.AppState.Home
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event
import org.n27.stonks.presentation.common.broadcast.Event.*
import org.n27.stonks.presentation.common.broadcast.EventBus

@OptIn(FlowPreview::class)
class AppViewModel(eventBus: EventBus) : ViewModel() {

    private val state = MutableStateFlow<AppState>(Home)
    internal val viewState = state.asStateFlow()

    private val event = Channel<AppEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    init {
        eventBus.events
            .onEach(::handleEvent)
            .launchIn(viewModelScope)
    }

    private fun handleEvent(e: Event) = when (e) {
        GoBack -> state.value = Home
        is NavigateToDetail -> state.value = Detail(e.symbol)
        is ShowErrorNotification -> event.trySend(AppEvent.ShowErrorNotification(e.title))
    }
}
