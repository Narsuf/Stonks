package org.n27.stonks.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.data.remote.model.FredObservationsResponse
import org.n27.stonks.data.remote.model.MacroIndicatorRaw

private const val FRED_API_URL = "https://api.stlouisfed.org/fred/series/observations"

private const val OBSERVATIONS_LIMIT = 10

class FredApi(
    private val apiKey: String?,
    private val httpClient: HttpClient,
) {

    suspend fun getTreasuryYield10Y(): MacroIndicatorRaw = fetchYield("DGS10")

    suspend fun getEuropeanTreasuryYield10Y(): MacroIndicatorRaw = fetchYield("IRLTLT01EZM156N")

    suspend fun getCorporateBondYieldAAA(): MacroIndicatorRaw = fetchYield("AAA")

    private suspend fun fetchYield(id: String): MacroIndicatorRaw {
        if (apiKey.isNullOrBlank()) error("FRED API key is not configured (set FRED_API_KEY)")

        val response = httpClient.get(FRED_API_URL) {
            parameter("series_id", id)
            parameter("api_key", apiKey)
            parameter("file_type", "json")
            parameter("sort_order", "desc")
            parameter("limit", OBSERVATIONS_LIMIT)
        }.body<FredObservationsResponse>()

        return response.observations.firstNotNullOfOrNull { observation ->
            observation.value.toDoubleOrNull()?.let { MacroIndicatorRaw(it, observation.date) }
        } ?: error("FRED response for '$id' contained no numeric values")
    }
}
