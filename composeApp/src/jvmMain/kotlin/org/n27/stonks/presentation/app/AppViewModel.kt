package org.n27.stonks.presentation.app

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.parametersOf
import org.n27.stonks.presentation.app.entities.AppEvent
import org.n27.stonks.presentation.app.entities.AppState
import org.n27.stonks.presentation.app.entities.AppState.Idle
import org.n27.stonks.presentation.app.entities.AppState.Screen.Detail
import org.n27.stonks.presentation.app.entities.AppState.Screen.Search
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event
import org.n27.stonks.presentation.common.broadcast.Event.*
import org.n27.stonks.presentation.common.broadcast.EventBus

@OptIn(FlowPreview::class)
class AppViewModel(eventBus: EventBus) : ViewModel() {

    private val stack = mutableStateListOf<AppState>()
    private val state = MutableStateFlow<AppState>(Idle)
    internal val viewState = state.asStateFlow()

    private val event = Channel<AppEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    private val koin = GlobalContext.get()

    init {
        push(Search(koin.get()))
        //push(Detail(koin.get { parametersOf("AAPL") }))
        eventBus.events
            .onEach(::handleEvent)
            .launchIn(viewModelScope)
    }

    private fun handleEvent(e: Event) = when (e) {
        GoBack -> pop()
        is NavigateToDetail -> push(Detail(koin.get { parametersOf(e.symbol) }))
        is ShowErrorNotification -> event.trySend(AppEvent.ShowErrorNotification(e.title))
    }

    private fun push(screen: AppState) {
        stack.add(screen)
        state.value = stack.last()
    }

    private fun pop() {
        if (stack.size > 1) {
            stack.removeLast()
            state.value = stack.last()
        }
    }
}
