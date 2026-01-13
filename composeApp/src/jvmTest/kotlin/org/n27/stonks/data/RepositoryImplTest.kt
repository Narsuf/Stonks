package org.n27.stonks.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.n27.stonks.domain.Repository
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.domain.getStocks

class RepositoryImplTest {

    private lateinit var repository: Repository
    private lateinit var api: Api

    @Before
    fun setUp() {
        api = mock(Api::class.java)
        repository = RepositoryImpl(api)
    }

    @Test
    fun `getStock should return stock`() = runBlocking {
        `when`(api.getStock(anyString())).thenReturn(getStock())

        val result = repository.getStock("AAPL")

        assertEquals(getStock(), result.getOrNull())
    }

    @Test
    fun `getStocks should return stocks`() = runBlocking {
        `when`(api.getStocks(anyBoolean(), anyString(), anyInt(), anyInt())).thenReturn(getStocks())

        val result = repository.getStocks(false, "AAPL", 0, 10)

        assertEquals(getStocks(), result.getOrNull())
    }

    @Test
    fun `getWatchlist should return watchlist`() = runBlocking {
        `when`(api.getWatchlist(anyInt(), anyInt())).thenReturn(getStocks())

        val result = repository.getWatchlist(0, 10)

        assertEquals(getStocks(), result.getOrNull())
    }

    @Test
    fun `addToWatchlist should call api`() = runBlocking {
        val symbol = "AAPL"

        repository.addToWatchlist(symbol)

        verify(api).addToWatchlist(symbol)
    }

    @Test
    fun `removeFromWatchlist should call api`() = runBlocking {
        val symbol = "AAPL"

        repository.removeFromWatchlist(symbol)

        verify(api).removeFromWatchlist(symbol)
    }

    @Test
    fun `editWatchlistItem should call api`() = runBlocking {
        val symbol = "AAPL"
        val epsGrowth = 7.7
        val valuationFloor = 16.0

        repository.editWatchlistItem(symbol, epsGrowth, valuationFloor)

        verify(api).addCustomValues(symbol, epsGrowth, valuationFloor)
    }
}
