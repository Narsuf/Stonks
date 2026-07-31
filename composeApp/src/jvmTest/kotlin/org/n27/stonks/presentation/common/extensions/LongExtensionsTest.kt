package org.n27.stonks.presentation.common.extensions

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.assertEquals

class LongExtensionsTest {

    private val epochMilli = Instant.parse("2026-03-30T14:05:00Z").toEpochMilli()

    @Test
    fun `toDateString should format using the default pattern in the given zone`() {
        assertEquals("30. Mar 14:05", epochMilli.toDateString(zone = ZoneOffset.UTC))
    }

    @Test
    fun `toDateString should format using a custom pattern`() {
        assertEquals("2026-03-30", epochMilli.toDateString(pattern = "yyyy-MM-dd", zone = ZoneOffset.UTC))
    }

    @Test
    fun `toDateString should shift the time when a different zone is used`() {
        assertEquals("30. Mar 09:05", epochMilli.toDateString(zone = ZoneOffset.ofHours(-5)))
    }
}
