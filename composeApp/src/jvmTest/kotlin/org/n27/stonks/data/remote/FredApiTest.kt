package org.n27.stonks.data.remote

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.n27.stonks.data.remote.model.MacroIndicatorRaw
import kotlin.test.assertEquals

class FredApiTest {

    private lateinit var api: FredApi

    private fun jsonClient(engine: MockEngine) = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private fun observations(latest: String) = """
        {
          "observations": [
            { "date": "2024-01-02", "value": "." },
            { "date": "2024-01-01", "value": "$latest" }
          ]
        }
    """.trimIndent()

    @BeforeEach
    fun setup() {
        val mockEngine = MockEngine { request ->
            val value = when (request.url.parameters["series_id"]) {
                "DGS10" -> "4.5"
                "IRLTLT01EZM156N" -> "3.1"
                "AAA" -> "5.2"
                else -> error("Unhandled ${request.url}")
            }
            respond(
                content = ByteReadChannel(observations(value)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        api = FredApi("test-key", jsonClient(mockEngine))
    }

    @Test
    fun `getTreasuryYield10Y should return the latest available value and date`() = runTest {
        assertEquals(MacroIndicatorRaw(4.5, "2024-01-01"), api.getTreasuryYield10Y())
    }

    @Test
    fun `getEuropeanTreasuryYield10Y should return the latest available value and date`() = runTest {
        assertEquals(MacroIndicatorRaw(3.1, "2024-01-01"), api.getEuropeanTreasuryYield10Y())
    }

    @Test
    fun `getCorporateBondYieldAAA should return the latest available value and date`() = runTest {
        assertEquals(MacroIndicatorRaw(5.2, "2024-01-01"), api.getCorporateBondYieldAAA())
    }
}
