package org.n27.stonks.presentation.common.broadcast

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.n27.stonks.presentation.common.broadcast.Event.GoBack
import org.n27.stonks.utils.test

@ExperimentalCoroutinesApi
class EventBusTest {

    private val eventBus = EventBus()

    @Test
    fun `should emit events`() = runTest {
        val observer = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))

        eventBus.emit(GoBack())

        observer.assertValues(GoBack())
        observer.close()
    }
}
