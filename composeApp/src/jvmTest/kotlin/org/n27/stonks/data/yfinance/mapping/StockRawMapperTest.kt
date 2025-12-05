package org.n27.stonks.data.yfinance.mapping

import org.junit.Test
import org.n27.stonks.test_data.data.getStockRaw
import org.n27.stonks.test_data.domain.getStock
import kotlin.test.assertEquals

class StockRawMapperTest {

    @Test
    fun `should return expected value`() {
        val expected = getStock()

        val actual = getStockRaw().toDomainEntity(null)

        assertEquals(expected, actual)
    }
}
