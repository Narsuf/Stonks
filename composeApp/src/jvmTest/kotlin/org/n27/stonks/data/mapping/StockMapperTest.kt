package org.n27.stonks.data.mapping

import org.junit.jupiter.api.Test
import org.n27.stonks.test_data.data.getStockRaw
import org.n27.stonks.test_data.data.getStocksRaw
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.domain.getStocks
import kotlin.test.assertEquals

class StockMapperTest {

    @Test
    fun `toDomain should map StockRaw to Stock correctly`() {
        assertEquals(getStock(), getStockRaw().toDomain())
    }

    @Test
    fun `toDomain should map StocksRaw to Stocks correctly`() {
        assertEquals(getStocks(), getStocksRaw().toDomain())
    }
}
