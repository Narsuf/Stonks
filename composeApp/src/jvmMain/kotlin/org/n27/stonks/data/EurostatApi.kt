package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

private const val GERMAN_CPI_URL = "https://ec.europa.eu/eurostat/api/dissemination/statistics/1.0/data/prc_hicp_manr?geo=DE&coicop=CP00&format=JSON"

class EurostatApi(private val httpClient: HttpClient) {

    suspend fun getGermanCpiYoY(): Double {
        val response = httpClient.get(GERMAN_CPI_URL).body<EurostatResponse>()
        return response.value.maxBy { it.key.toInt() }.value
    }
}

@Serializable
private data class EurostatResponse(val value: Map<String, Double>)
