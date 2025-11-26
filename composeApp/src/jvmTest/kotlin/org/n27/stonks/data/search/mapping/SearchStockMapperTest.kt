package org.n27.stonks.data.search.mapping

import org.junit.Test
import org.n27.stonks.test_data.data.getSearchStockRaw
import org.n27.stonks.test_data.domain.getSearch
import kotlin.test.assertEquals

class SearchStockMapperTest {

    @Test
    fun `should return expected value`() {
        val expected = getSearch()

        val actual = listOf(getSearchStockRaw()).toDomainEntity(1)

        assertEquals(expected, actual)
    }
}
