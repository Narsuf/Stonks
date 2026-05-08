package org.n27.stonks.data.remote.mapping

import org.junit.jupiter.api.Test
import org.n27.stonks.data.remote.mapping.mapToMacroIndicators
import org.n27.stonks.data.remote.mapping.toDomain
import org.n27.stonks.test_data.data.getMacroIndicatorRaw
import org.n27.stonks.test_data.domain.getMacroIndicator
import org.n27.stonks.test_data.domain.getMacroIndicators
import kotlin.test.assertEquals

class MacroIndicatorsMapperTest {

    @Test
    fun `toDomain should map MacroIndicatorRaw to MacroIndicator correctly`() {
        assertEquals(getMacroIndicator(), getMacroIndicatorRaw().toDomain())
    }

    @Test
    fun `mapToMacroIndicators should map all raw indicators to MacroIndicators correctly`() {
        val expected = mapToMacroIndicators(
            treasury = getMacroIndicatorRaw(date = "2026-03-30"),
            europeanTreasury = getMacroIndicatorRaw(),
            corporate = getMacroIndicatorRaw(value = 3.0),
            germanCpi = getMacroIndicatorRaw(value = 1.9, date = "2025-12"),
        )

        assertEquals(expected, getMacroIndicators())
    }
}
