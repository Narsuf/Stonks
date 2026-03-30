package org.n27.stonks.data.fred

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.n27.stonks.domain.models.FredYields
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals

class FredYieldsStoreTest {

    private val fredYields = FredYields(treasury10Y = 4.5, corporateAAA = 5.2)
    private val fredApi = mock<FredApi>()
    private val cache = mock<FredYieldsCache>()
    private val store = FredYieldsStore(fredApi, cache)

    @Test
    fun `refresh should emit cached yields and skip api when saved today`() = runTest {
        whenever(cache.load()).thenReturn(System.currentTimeMillis() to fredYields)

        store.refresh()

        assertEquals(fredYields, store.yields.value)
        verify(fredApi, never()).getTreasuryYield10Y()
        verify(fredApi, never()).getCorporateBondYieldAAA()
    }

    @Test
    fun `refresh should call api and save when cache is from yesterday`() = runTest {
        val yesterday = LocalDate.now(ZoneId.systemDefault())
            .minusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        whenever(cache.load()).thenReturn(yesterday to fredYields)
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(fredYields.treasury10Y)
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(fredYields.corporateAAA)

        store.refresh()

        assertEquals(fredYields, store.yields.value)
        verify(fredApi).getTreasuryYield10Y()
        verify(fredApi).getCorporateBondYieldAAA()
        verify(cache).save(fredYields)
    }

    @Test
    fun `refresh should call api and save when cache is empty`() = runTest {
        whenever(cache.load()).thenReturn(null)
        whenever(fredApi.getTreasuryYield10Y()).thenReturn(fredYields.treasury10Y)
        whenever(fredApi.getCorporateBondYieldAAA()).thenReturn(fredYields.corporateAAA)

        store.refresh()

        assertEquals(fredYields, store.yields.value)
        verify(fredApi).getTreasuryYield10Y()
        verify(fredApi).getCorporateBondYieldAAA()
        verify(cache).save(fredYields)
    }
}
