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
import org.n27.stonks.data.remote.bundesbank.mapping.toDomain
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator
import org.n27.stonks.utils.getJson
import kotlin.test.assertEquals

class BundesbankApiTest {

    private lateinit var api: BundesbankApi

    @BeforeEach
    fun setup() {
        val mockEngine = MockEngine { request ->
            val content = when {
                request.url.encodedPath.contains("BBSIS") -> getJson("bundesbank-bund-yield.json")
                request.url.encodedPath.contains("BBDP1") -> getJson("bundesbank-german-cpi.json")
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
        assertEquals(MacroIndicator(3.16, "2026-07-27"), api.getGermanBundYield10Y().toDomain())
    }

    @Test
    fun `getGermanCpiYoY should return the latest available value and date`() = runTest {
        assertEquals(MacroIndicator(2.3, "2026-06"), api.getGermanCpiYoY().toDomain())
    }
}
