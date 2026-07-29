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

    @BeforeEach
    fun setup() {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(
                    """
                    {
                        "data": {
                            "structure": {
                                "dimensions": {
                                    "observation": [
                                        {
                                            "values": [
                                                { "id": "2026-07-24" },
                                                { "id": "2026-07-25" },
                                                { "id": "2026-07-26" },
                                                { "id": "2026-07-27" }
                                            ]
                                        }
                                    ]
                                }
                            },
                            "dataSets": [
                                {
                                    "series": {
                                        "0:0:0:0:0:0:0:0:0:0:0:0:0:0:0": {
                                            "observations": {
                                                "0": ["3.21", 10, 0],
                                                "1": [null, 0, 1],
                                                "2": [null, 0, 1],
                                                "3": ["3.16", 11, 0]
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                    }
                    """.trimIndent()
                ),
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
}
