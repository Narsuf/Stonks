package org.n27.stonks.data.fred

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FredApiTest {

    private lateinit var api: FredApi
    private lateinit var mockEngine: MockEngine

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.parameters["id"]) {
                "DGS10" -> respond(
                    content = ByteReadChannel("DATE,DGS10\n2024-01-01,4.5\n2024-01-02,."),
                    status = HttpStatusCode.OK,
                )
                "AAA" -> respond(
                    content = ByteReadChannel("DATE,AAA\n2024-01-01,5.2\n2024-01-02,."),
                    status = HttpStatusCode.OK,
                )
                else -> error("Unhandled ${request.url}")
            }
        }

        api = FredApi(HttpClient(mockEngine))
    }

    @Test
    fun `getTreasuryYield10Y should return the latest available value`() = runTest {
        assertEquals(4.5, api.getTreasuryYield10Y(), 0.0)
    }

    @Test
    fun `getCorporateBondYieldAAA should return the latest available value`() = runTest {
        assertEquals(5.2, api.getCorporateBondYieldAAA(), 0.0)
    }
}
