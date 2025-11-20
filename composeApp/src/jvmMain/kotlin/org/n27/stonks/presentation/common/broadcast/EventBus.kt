package org.n27.stonks.presentation.common.broadcast

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBus {

    private val eventsFlow = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = eventsFlow.asSharedFlow()

    suspend fun emit(event: Event) { eventsFlow.emit(event) }
}
