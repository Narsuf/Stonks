package org.n27.stonks.data

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.n27.stonks.data.eurostat.EurostatApi
import org.n27.stonks.test_data.data.getMacroIndicatorRaw
import org.n27.stonks.test_data.domain.getMacroIndicator
import org.n27.stonks.test_data.domain.getMacroIndicators
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals

class MacroIndicatorsStoreTest {

    private val indicators = getMacroIndicators(
        treasury10Y = getMacroIndicator(4.5, "2026-03-30"),
        europeanTreasury10Y = getMacroIndicator(3.1, "2026-01-01"),
        corporateAAA = getMacroIndicator(5.2, "2026-01-01"),
        germanCpi = getMacroIndicator(2.0, "2025-12"),
    )
    private val fredApi = mock<FredApi>()
    private val eurostatApi = mock<EurostatApi>()
    private val cache = mock<MacroIndicatorsCache>()
    private val store = MacroIndicatorsStore(fredApi, eurostatApi, cache)

    @Test
    fun `refresh should emit cached indicators and skip api when saved today`() = runTest {
        whenever(cache.load()).thenReturn(System.currentTimeMillis() to indicators)

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(fredApi, never()).getTreasuryYield10Y()
        verify(fredApi, never()).getEuropeanTreasuryYield10Y()
        verify(fredApi, never()).getCorporateBondYieldAAA()
        verify(eurostatApi, never()).getGermanCpiYoY()
    }

    @Test
    fun `refresh should call api and save when cache is from yesterday`() = runTest {
        val yesterday = LocalDate.now(ZoneId.systemDefault())
            .minusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        whenever(cache.load()).thenReturn(yesterday to indicators)
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.treasury10Y.value, indicators.treasury10Y.date))
        whenever(fredApi.getEuropeanTreasuryYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.europeanTreasury10Y.value, indicators.europeanTreasury10Y.date))
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(getMacroIndicatorRaw(indicators.corporateAAA.value, indicators.corporateAAA.date))
        whenever(eurostatApi.getGermanCpiYoY()).thenReturn(getMacroIndicatorRaw(indicators.germanCpi.value, indicators.germanCpi.date))

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(fredApi).getTreasuryYield10Y()
        verify(fredApi).getEuropeanTreasuryYield10Y()
        verify(fredApi).getCorporateBondYieldAAA()
        verify(eurostatApi).getGermanCpiYoY()
        verify(cache).save(indicators)
    }

    @Test
    fun `refresh should call api and save when cache is empty`() = runTest {
        whenever(cache.load()).thenReturn(null)
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.treasury10Y.value, indicators.treasury10Y.date))
        whenever(fredApi.getEuropeanTreasuryYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.europeanTreasury10Y.value, indicators.europeanTreasury10Y.date))
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(getMacroIndicatorRaw(indicators.corporateAAA.value, indicators.corporateAAA.date))
        whenever(eurostatApi.getGermanCpiYoY()).thenReturn(getMacroIndicatorRaw(indicators.germanCpi.value, indicators.germanCpi.date))

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(fredApi).getTreasuryYield10Y()
        verify(fredApi).getEuropeanTreasuryYield10Y()
        verify(fredApi).getCorporateBondYieldAAA()
        verify(eurostatApi).getGermanCpiYoY()
        verify(cache).save(indicators)
    }
}
