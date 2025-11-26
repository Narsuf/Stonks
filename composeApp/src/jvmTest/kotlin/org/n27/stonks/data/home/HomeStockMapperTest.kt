package org.n27.stonks.data.home

import org.junit.Test
import org.n27.stonks.test_data.data.getSearchStockRaw
import org.n27.stonks.test_data.domain.getHome
import kotlin.test.assertEquals

class HomeStockMapperTest {

    @Test
    fun `should return expected value`() {
        val expected = getHome()

        val actual = listOf(getSearchStockRaw()).toDomainEntity()

        assertEquals(expected, actual)
    }
}
