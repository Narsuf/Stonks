package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.domain.getStocks
import org.n27.stonks.utils.getJson
import kotlin.test.assertEquals

class ApiTest {

    private lateinit var api: Api
    private lateinit var mockEngine: MockEngine

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/stock/AAPL" -> respond(
                    content = ByteReadChannel(getJson("stock.json")),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
                "/stocks" -> respond(
                    content = ByteReadChannel(getJson("stocks.json")),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
                "/watchlist" -> respond(
                    content = ByteReadChannel(getJson("stocks.json")),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
                "/watchlist/AAPL" -> respond(
                    content = ByteReadChannel(""),
                    status = HttpStatusCode.OK
                )
                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json()
            }
        }

        api = Api("http://localhost:", httpClient)
    }

    @Test
    fun `getStock should return a stock`() = runTest {
        val actual = api.getStock("AAPL")

        assertEquals(
            expected = getStock(
                expectedEpsGrowth = 7.75,
                valuationFloor = 12.5,
                currentIntrinsicValue = 185.03,
                forwardIntrinsicValue = 177.83,
                lastUpdated = 1765627200000,
            ),
            actual = actual,
        )
    }

    @Test
    fun `getStocks should return stocks`() = runTest {
        val actual = api.getStocks(null, null, false)

        assertEquals(getStocks(), actual)
    }

    @Test
    fun `getWatchlist should return stocks`() = runTest {
        val actual = api.getWatchlist(null, null)

        assertEquals(getStocks(), actual)
    }

    @Test
    fun `addToWatchlist should complete without error`() = runTest {
        api.addToWatchlist("AAPL")
    }

    @Test
    fun `removeFromWatchlist should complete without error`() = runTest {
        api.removeFromWatchlist("AAPL")
    }
}
