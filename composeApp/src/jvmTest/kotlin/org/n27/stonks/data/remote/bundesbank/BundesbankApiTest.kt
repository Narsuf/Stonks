package org.n27.stonks.data.remote.bundesbank

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

class BundesbankApiTest {

    private lateinit var api: BundesbankApi

    private fun observations(dates: List<String>, values: String) = """
        {
            "data": {
                "structure": {
                    "dimensions": {
                        "observation": [
                            { "values": [ ${dates.joinToString(",") { "{ \"id\": \"$it\" }" }} ] }
                        ]
                    }
                },
                "dataSets": [
                    { "series": { "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0": { "observations": { $values } } } }
                ]
            }
        }
    """.trimIndent()

    @BeforeEach
    fun setup() {
        val mockEngine = MockEngine { request ->
            val content = when {
                request.url.encodedPath.contains("BBSIS") -> observations(
                    dates = listOf("2026-07-24", "2026-07-25", "2026-07-26", "2026-07-27"),
                    values = """"0": ["3.21", 10, 0], "1": [null, 0, 1], "2": [null, 0, 1], "3": ["3.16", 11, 0]""",
                )
                request.url.encodedPath.contains("BBDP1") -> observations(
                    dates = listOf("2026-04", "2026-05", "2026-06"),
                    values = """"0": ["2.9", 1, 0], "1": ["2.6", 1, 0], "2": ["2.3", 1, 0]""",
                )
                else -> error("Unhandled ${request.url}")
            }
            respond(
                content = ByteReadChannel(content),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        api = BundesbankApi(HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        })
    }

    @Test
    fun `getGermanBundYield10Y should skip null observations and return the latest available value and date`() = runTest {
        assertEquals(MacroIndicatorRaw(3.16, "2026-07-27"), api.getGermanBundYield10Y())
    }

    @Test
    fun `getGermanCpiYoY should return the latest available value and date`() = runTest {
        assertEquals(MacroIndicatorRaw(2.3, "2026-06"), api.getGermanCpiYoY())
    }
}
