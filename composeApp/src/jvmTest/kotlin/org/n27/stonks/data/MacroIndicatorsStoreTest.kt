package org.n27.stonks.data

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.n27.stonks.data.eurostat.EurostatApi
import org.n27.stonks.data.models.MacroIndicatorRaw
import org.n27.stonks.domain.models.MacroIndicators
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals

class MacroIndicatorsStoreTest {

    private val indicators = MacroIndicators(
        treasury10Y = 4.5,
        treasury10YDate = "2026-03-30",
        europeanTreasury10Y = 3.1,
        europeanTreasury10YDate = "2026-01-01",
        corporateAAA = 5.2,
        germanCpi = 2.0,
        germanCpiDate = "2025-12",
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
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(MacroIndicatorRaw(indicators.treasury10Y, indicators.treasury10YDate!!))
        whenever(fredApi.getEuropeanTreasuryYield10Y()).thenReturn(MacroIndicatorRaw(indicators.europeanTreasury10Y, indicators.europeanTreasury10YDate!!))
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(MacroIndicatorRaw(indicators.corporateAAA, "2026-01-01"))
        whenever(eurostatApi.getGermanCpiYoY()).thenReturn(MacroIndicatorRaw(indicators.germanCpi!!, indicators.germanCpiDate!!))

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
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(MacroIndicatorRaw(indicators.treasury10Y, indicators.treasury10YDate!!))
        whenever(fredApi.getEuropeanTreasuryYield10Y()).thenReturn(MacroIndicatorRaw(indicators.europeanTreasury10Y, indicators.europeanTreasury10YDate!!))
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(MacroIndicatorRaw(indicators.corporateAAA, "2026-01-01"))
        whenever(eurostatApi.getGermanCpiYoY()).thenReturn(MacroIndicatorRaw(indicators.germanCpi!!, indicators.germanCpiDate!!))

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(fredApi).getTreasuryYield10Y()
        verify(fredApi).getEuropeanTreasuryYield10Y()
        verify(fredApi).getCorporateBondYieldAAA()
        verify(eurostatApi).getGermanCpiYoY()
        verify(cache).save(indicators)
    }
}
