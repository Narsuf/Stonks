package org.n27.stonks.data.fred

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

private const val FRED_BASE_URL = "https://fred.stlouisfed.org/graph/fredgraph.csv?id="

class FredApi(private val httpClient: HttpClient) {

    suspend fun getTreasuryYield10Y(): Double = fetchYield("DGS10")

    suspend fun getCorporateBondYieldAAA(): Double = fetchYield("AAA")

    private suspend fun fetchYield(id: String): Double {
        val csv = httpClient.get("$FRED_BASE_URL$id").bodyAsText()
        return csv.lines()
            .asReversed()
            .firstNotNullOf { line -> line.substringAfterLast(",").toDoubleOrNull() }
    }
}
