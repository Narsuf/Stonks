package org.n27.stonks.data.remote.bundesbank

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.n27.stonks.data.remote.bundesbank.mapping.toRaw
import org.n27.stonks.data.remote.bundesbank.model.BundesbankResponse
import org.n27.stonks.data.remote.model.MacroIndicatorRaw
import java.time.LocalDate

private const val BUNDESBANK_API_URL =
    "https://api.statistiken.bundesbank.de/rest/data/BBSIS/D.I.ZAR.ZI.EUR.S1311.B.A604.R10XX.R.A.A._Z._Z.A"

private const val LOOKBACK_DAYS = 10L

class BundesbankApi(private val httpClient: HttpClient) {

    suspend fun getGermanBundYield10Y(): MacroIndicatorRaw = httpClient.get(BUNDESBANK_API_URL) {
        header(HttpHeaders.Accept, "application/vnd.sdmx.data+json;version=1.0.0")
        parameter("startPeriod", LocalDate.now().minusDays(LOOKBACK_DAYS).toString())
    }.body<BundesbankResponse>().toRaw()
}
