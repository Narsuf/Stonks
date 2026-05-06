package org.n27.stonks.data.remote.eurostat

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
import org.n27.stonks.data.remote.eurostat.EurostatApi
import org.n27.stonks.data.remote.model.MacroIndicatorRaw
import kotlin.test.assertEquals

class EurostatApiTest {

    private lateinit var api: EurostatApi

    @BeforeEach
    fun setup() {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel("""
                    {
                        "value": {"0": 1.9, "1": 2.3, "2": 2.0},
                        "dimension": {
                            "time": {
                                "category": {
                                    "index": {"2025-10": 0, "2025-11": 1, "2025-12": 2}
                                }
                            }
                        }
                    }
                """.trimIndent()),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        api = EurostatApi(HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        })
    }

    @Test
    fun `getGermanCpiYoY should return the latest available value and date`() = runTest {
        assertEquals(MacroIndicatorRaw(2.0, "2025-12"), api.getGermanCpiYoY())
    }
}
