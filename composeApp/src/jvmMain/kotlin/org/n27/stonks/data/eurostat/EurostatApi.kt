package org.n27.stonks.data.eurostat

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.data.eurostat.mapping.toRaw
import org.n27.stonks.data.eurostat.models.EurostatResponse
import org.n27.stonks.data.models.MacroIndicatorRaw

private const val GERMAN_CPI_URL = "https://ec.europa.eu/eurostat/api/dissemination/statistics/1.0/data/prc_hicp_manr?geo=DE&coicop=CP00&format=JSON"

class EurostatApi(private val httpClient: HttpClient) {

    suspend fun getGermanCpiYoY(): MacroIndicatorRaw =
        httpClient.get(GERMAN_CPI_URL).body<EurostatResponse>().toRaw()
}
