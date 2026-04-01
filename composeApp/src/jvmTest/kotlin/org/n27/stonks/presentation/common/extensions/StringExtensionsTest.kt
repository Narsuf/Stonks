package org.n27.stonks.presentation.common.extensions

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringExtensionsTest {

    @Test
    fun `toFormattedDate should format daily date`() {
        assertEquals("30 Mar 2026", "2026-03-30".toFormattedDate())
    }

    @Test
    fun `toFormattedDate should format monthly FRED date as month and year`() {
        assertEquals("Jan 2026", "2026-01-01".toFormattedDate())
    }

    @Test
    fun `toFormattedDate should format year-month date`() {
        assertEquals("Dec 2025", "2025-12".toFormattedDate())
    }

    @Test
    fun `toFormattedDate should return original string when unparseable`() {
        assertEquals("not-a-date", "not-a-date".toFormattedDate())
    }
}
