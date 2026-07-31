package org.n27.stonks.presentation.common.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FlowExtensionsTest {

    private sealed interface State {
        data class Loading(val progress: Int) : State
        data class Loaded(val value: String) : State
    }

    @Test
    fun `updateIfType should apply the function when the current value matches the type`() {
        val flow = MutableStateFlow<State>(State.Loading(progress = 0))

        flow.updateIfType<State, State.Loading> { it.copy(progress = it.progress + 1) }

        assertEquals(State.Loading(progress = 1), flow.value)
    }

    @Test
    fun `updateIfType should leave the value unchanged when it does not match the type`() {
        val flow = MutableStateFlow<State>(State.Loaded(value = "stock"))

        flow.updateIfType<State, State.Loading> { it.copy(progress = it.progress + 1) }

        assertEquals(State.Loaded(value = "stock"), flow.value)
    }
}
