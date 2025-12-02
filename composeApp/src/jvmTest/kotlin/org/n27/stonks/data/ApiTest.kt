package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import org.n27.stonks.test_data.data.getStockRaw
import org.n27.stonks.utils.getJson
import kotlin.test.assertEquals

class ApiTest {

    @Test
    fun `getStocks should return a list of stocks`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(getJson("stocks.json")),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val api = Api(createHttpClient(mockEngine), "https://api.stonks.com")
        val expected = listOf(getStockRaw())

        val actual = api.getStocks("AAPL")

        assertEquals(expected, actual)
    }

    @Test
    fun `getStock should return a stock`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(getJson("stock.json")),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val api = Api(createHttpClient(mockEngine), "https://api.stonks.com")

        val actual = api.getStock("AAPL")

        assertEquals(getStockRaw(), actual)
    }
}

internal fun createHttpClient(mockEngine: MockEngine) = HttpClient(mockEngine) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}
