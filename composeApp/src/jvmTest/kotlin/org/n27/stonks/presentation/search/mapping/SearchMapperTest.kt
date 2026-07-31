package org.n27.stonks.presentation.search.mapping

import org.junit.jupiter.api.Test
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.domain.getStocks
import org.n27.stonks.test_data.presentation.getSearchContent
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SearchMapperTest {

    @Test
    fun `toContent should map Stocks to Content correctly`() {
        val result = getStocks().toContent(isEndReached = false)

        assertEquals(getSearchContent(), result)
    }

    @Test
    fun `toContent should return empty items when Stocks items is empty`() {
        val result = getStocks(items = emptyList()).toContent(isEndReached = false)

        assertTrue(result.items.isEmpty())
    }

    @Test
    fun `toPresentationEntity should set icon to null when logo is null`() {
        val stock = getStock(logo = null)

        val result = listOf(stock).toPresentationEntity()[0]

        assertNull(result.icon)
    }

    @Test
    fun `toPresentationEntity should truncate the company name after a triple space`() {
        val stock = getStock(companyName = "Apple Inc.   Common Stock")

        val result = listOf(stock).toPresentationEntity()[0]

        assertEquals("Apple Inc.", result.name)
    }
}
