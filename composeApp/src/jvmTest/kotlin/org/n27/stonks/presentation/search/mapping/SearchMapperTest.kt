package org.n27.stonks.presentation.search.mapping

import org.junit.jupiter.api.Test
import org.n27.stonks.test_data.domain.getStocks
import org.n27.stonks.test_data.presentation.getSearchContent
import kotlin.test.assertEquals

class SearchMapperTest {

    @Test
    fun `toContent should map Stocks to Content correctly`() {
        val result = getStocks().toContent(isEndReached = false)

        assertEquals(getSearchContent(), result)
    }
}
