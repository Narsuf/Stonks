package org.n27.stonks.data

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.n27.stonks.data.persistence.MacroIndicatorsCache
import org.n27.stonks.data.remote.bundesbank.BundesbankApi
import org.n27.stonks.test_data.data.getBundesbankResponse
import org.n27.stonks.test_data.domain.getMacroIndicators
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals

class MacroIndicatorsStoreTest {

    private val indicators = getMacroIndicators()
    private val bundesbankApi = mock<BundesbankApi>()
    private val cache = mock<MacroIndicatorsCache>()
    private val store = MacroIndicatorsStore(bundesbankApi, cache)

    @Test
    fun `refresh should emit cached indicators and skip api when saved today`() = runTest {
        whenever(cache.load()).thenReturn(System.currentTimeMillis() to indicators)

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(bundesbankApi, never()).getGermanBundYield10Y()
        verify(bundesbankApi, never()).getGermanCpiYoY()
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("staleCacheCases")
    fun `refresh should call api and save when cache is stale or empty`(
        timestamp: Long?,
        @Suppress("UNUSED_PARAMETER") description: String,
    ) = runTest {
        whenever(cache.load()).thenReturn(timestamp?.let { it to indicators })
        whenever(bundesbankApi.getGermanBundYield10Y()).thenReturn(getBundesbankResponse(indicators.bundYield10Y.value, indicators.bundYield10Y.date))
        whenever(bundesbankApi.getGermanCpiYoY()).thenReturn(getBundesbankResponse(indicators.germanCpi.value, indicators.germanCpi.date))

        store.refresh()

        assertEquals(indicators, store.indicators.value)
        verify(bundesbankApi).getGermanBundYield10Y()
        verify(bundesbankApi).getGermanCpiYoY()
        verify(cache).save(indicators)
    }

    @Test
    fun `refresh should not save or emit when an api call fails`() = runTest {
        whenever(cache.load()).thenReturn(null)
        whenever(bundesbankApi.getGermanBundYield10Y()).thenThrow(RuntimeException("Bundesbank is down"))
        whenever(bundesbankApi.getGermanCpiYoY()).thenReturn(getBundesbankResponse(indicators.germanCpi.value, indicators.germanCpi.date))

        store.refresh()

        assertEquals(null, store.indicators.value)
        verify(cache, never()).save(indicators)
    }

    companion object {
        @JvmStatic
        fun staleCacheCases() = listOf(
            Arguments.of(
                LocalDate.now(ZoneId.systemDefault())
                    .minusDays(1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli(),
                "cache is from yesterday",
            ),
            Arguments.of(null, "cache is empty"),
        )
    }
}
