package org.n27.stonks.data.remote.fred

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.data.remote.fred.model.FredObservationsResponse

private const val FRED_API_URL = "https://api.stlouisfed.org/fred/series/observations"
private const val REAL_TREASURY_YIELD_10Y_SERIES = "DFII10"
private const val OBSERVATIONS_LIMIT = 10

class FredApi(
    private val apiKey: String?,
    private val httpClient: HttpClient,
) {

    suspend fun getRealTreasuryYield10Y(): FredObservationsResponse = fetchLatest(REAL_TREASURY_YIELD_10Y_SERIES)

    private suspend fun fetchLatest(seriesId: String): FredObservationsResponse {
        if (apiKey.isNullOrBlank()) error("FRED API key is not configured (set FRED_API_KEY)")

        return httpClient.get(FRED_API_URL) {
            parameter("series_id", seriesId)
            parameter("api_key", apiKey)
            parameter("file_type", "json")
            parameter("sort_order", "desc")
            parameter("limit", OBSERVATIONS_LIMIT)
        }.body<FredObservationsResponse>()
    }
}
