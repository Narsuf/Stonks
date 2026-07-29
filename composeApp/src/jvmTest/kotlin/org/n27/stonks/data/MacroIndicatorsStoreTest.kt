package org.n27.stonks.data

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.n27.stonks.data.persistence.MacroIndicatorsCache
import org.n27.stonks.data.remote.FredApi
import org.n27.stonks.data.remote.bundesbank.BundesbankApi
import org.n27.stonks.test_data.data.getMacroIndicatorRaw
import org.n27.stonks.test_data.domain.getMacroIndicators
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals

class MacroIndicatorsStoreTest {

    private val indicators = getMacroIndicators()
    private val fredApi = mock<FredApi>()
    private val bundesbankApi = mock<BundesbankApi>()
    private val cache = mock<MacroIndicatorsCache>()
    private val store = MacroIndicatorsStore(fredApi, bundesbankApi, cache)

    @Test
    fun `refresh should emit cached indicators and skip api when saved today`() = runTest {
        whenever(cache.load()).thenReturn(System.currentTimeMillis() to indicators)

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(fredApi, never()).getTreasuryYield10Y()
        verify(bundesbankApi, never()).getGermanBundYield10Y()
        verify(fredApi, never()).getCorporateBondYieldAAA()
        verify(bundesbankApi, never()).getGermanCpiYoY()
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
        whenever(bundesbankApi.getGermanBundYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.europeanTreasury10Y.value, indicators.europeanTreasury10Y.date))
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(getMacroIndicatorRaw(indicators.corporateAAA.value, indicators.corporateAAA.date))
        whenever(bundesbankApi.getGermanCpiYoY()).thenReturn(getMacroIndicatorRaw(indicators.germanCpi.value, indicators.germanCpi.date))

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(fredApi).getTreasuryYield10Y()
        verify(bundesbankApi).getGermanBundYield10Y()
        verify(fredApi).getCorporateBondYieldAAA()
        verify(bundesbankApi).getGermanCpiYoY()
        verify(cache).save(indicators)
    }

    @Test
    fun `refresh should call api and save when cache is empty`() = runTest {
        whenever(cache.load()).thenReturn(null)
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.treasury10Y.value, indicators.treasury10Y.date))
        whenever(bundesbankApi.getGermanBundYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.europeanTreasury10Y.value, indicators.europeanTreasury10Y.date))
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(getMacroIndicatorRaw(indicators.corporateAAA.value, indicators.corporateAAA.date))
        whenever(bundesbankApi.getGermanCpiYoY()).thenReturn(getMacroIndicatorRaw(indicators.germanCpi.value, indicators.germanCpi.date))

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(fredApi).getTreasuryYield10Y()
        verify(bundesbankApi).getGermanBundYield10Y()
        verify(fredApi).getCorporateBondYieldAAA()
        verify(bundesbankApi).getGermanCpiYoY()
        verify(cache).save(indicators)
    }

    @Test
    fun `refresh should not save or emit when an api call fails`() = runTest {
        whenever(cache.load()).thenReturn(null)
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(getMacroIndicatorRaw(indicators.treasury10Y.value, indicators.treasury10Y.date))
        whenever(bundesbankApi.getGermanBundYield10Y()).thenThrow(RuntimeException("Bundesbank is down"))
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(getMacroIndicatorRaw(indicators.corporateAAA.value, indicators.corporateAAA.date))
        whenever(bundesbankApi.getGermanCpiYoY()).thenReturn(getMacroIndicatorRaw(indicators.germanCpi.value, indicators.germanCpi.date))

        store.refresh()

        assertEquals(null, store.indicators.value)
        verify(cache, never()).save(indicators)
    }
}
