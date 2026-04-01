package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FredApiTest {

    private lateinit var api: FredApi
    private lateinit var mockEngine: MockEngine

    @BeforeEach
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.parameters["id"]) {
                "DGS10" -> respond(
                    content = ByteReadChannel("DATE,DGS10\n2024-01-01,4.5\n2024-01-02,."),
                    status = HttpStatusCode.OK,
                )
                "IRLTLT01EZM156N" -> respond(
                    content = ByteReadChannel("DATE,IRLTLT01EZM156N\n2024-01-01,3.1\n2024-01-02,."),
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
        assertEquals(4.5, api.getTreasuryYield10Y())
    }

    @Test
    fun `getEuropeanTreasuryYield10Y should return the latest available value`() = runTest {
        assertEquals(3.1, api.getEuropeanTreasuryYield10Y())
    }

    @Test
    fun `getCorporateBondYieldAAA should return the latest available value`() = runTest {
        assertEquals(5.2, api.getCorporateBondYieldAAA())
    }
}
