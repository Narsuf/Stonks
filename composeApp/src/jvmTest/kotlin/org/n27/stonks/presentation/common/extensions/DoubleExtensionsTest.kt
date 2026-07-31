package org.n27.stonks.presentation.common.extensions

import org.junit.jupiter.api.Test
import org.n27.stonks.presentation.common.composables.DeltaState
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DoubleExtensionsTest {

    @Test
    fun `getTargetPrice should return null when intrinsicValue is null`() {
        assertNull(100.0.getTargetPrice(intrinsicValue = null, currency = "USD"))
    }

    @Test
    fun `getTargetPrice should return zero percentage when price is zero to avoid division by zero`() {
        val result = 0.0.getTargetPrice(intrinsicValue = 10.0, currency = "USD")!!

        assertEquals(DeltaState.POSITIVE, result.state)
        assertEquals("0.00 %", result.percentage)
    }

    @Test
    fun `getTargetPrice should return POSITIVE when intrinsicValue is above price`() {
        val result = 100.0.getTargetPrice(intrinsicValue = 150.0, currency = "USD")!!

        assertEquals(DeltaState.POSITIVE, result.state)
        assertEquals("50.00 %", result.percentage)
    }

    @Test
    fun `getTargetPrice should return NEGATIVE when intrinsicValue is below price`() {
        val result = 100.0.getTargetPrice(intrinsicValue = 50.0, currency = "USD")!!

        assertEquals(DeltaState.NEGATIVE, result.state)
        assertEquals("50.00 %", result.percentage)
    }

    @Test
    fun `getTargetPrice should return NEUTRAL when intrinsicValue equals price`() {
        val result = 100.0.getTargetPrice(intrinsicValue = 100.0, currency = "USD")!!

        assertEquals(DeltaState.NEUTRAL, result.state)
        assertEquals("0.00 %", result.percentage)
    }
}
