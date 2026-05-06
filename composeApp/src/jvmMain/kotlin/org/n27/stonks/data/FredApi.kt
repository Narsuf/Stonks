package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.n27.stonks.data.model.MacroIndicatorRaw

private const val FRED_BASE_URL = "https://fred.stlouisfed.org/graph/fredgraph.csv?id="

class FredApi(private val httpClient: HttpClient) {

    suspend fun getTreasuryYield10Y(): MacroIndicatorRaw = fetchYield("DGS10")

    suspend fun getEuropeanTreasuryYield10Y(): MacroIndicatorRaw = fetchYield("IRLTLT01EZM156N")

    suspend fun getCorporateBondYieldAAA(): MacroIndicatorRaw = fetchYield("AAA")

    private suspend fun fetchYield(id: String): MacroIndicatorRaw {
        val csv = httpClient.get("$FRED_BASE_URL$id").bodyAsText()
        return csv.lines()
            .asReversed()
            .firstNotNullOf { line ->
                val value = line.substringAfterLast(",").toDoubleOrNull()
                val date = line.substringBefore(",")
                value?.let { MacroIndicatorRaw(it, date) }
            }
    }
}
