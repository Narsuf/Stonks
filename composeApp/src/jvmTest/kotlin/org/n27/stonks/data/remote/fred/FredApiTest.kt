package org.n27.stonks.data.remote.fred

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
import org.n27.stonks.data.remote.fred.mapping.toDomain
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FredApiTest {

    private lateinit var api: FredApi

    private fun observations(latest: String) = """
        {
          "observations": [
            { "date": "2026-01-02", "value": "." },
            { "date": "2026-01-01", "value": "$latest" }
          ]
        }
    """.trimIndent()

    private fun mockApi(apiKey: String?) = FredApi(
        apiKey = apiKey,
        httpClient = HttpClient(MockEngine { request ->
            val value = when (request.url.parameters["series_id"]) {
                "DFII10" -> "1.8"
                else -> error("Unhandled ${request.url}")
            }
            respond(
                content = ByteReadChannel(observations(value)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        },
    )

    @BeforeEach
    fun setup() {
        api = mockApi(apiKey = "test-key")
    }

    @Test
    fun `getRealTreasuryYield10Y should skip non numeric observations and return the latest available value and date`() = runTest {
        assertEquals(MacroIndicator(1.8, "2026-01-01"), api.getRealTreasuryYield10Y().toDomain())
    }

    @Test
    fun `getRealTreasuryYield10Y should fail when the api key is missing`() = runTest {
        assertFailsWith<IllegalStateException> { mockApi(apiKey = null).getRealTreasuryYield10Y() }
    }
}
