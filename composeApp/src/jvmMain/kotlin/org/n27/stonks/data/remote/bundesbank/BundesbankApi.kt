package org.n27.stonks.data.remote.bundesbank

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.n27.stonks.data.remote.bundesbank.mapping.toRaw
import org.n27.stonks.data.remote.bundesbank.model.BundesbankResponse
import org.n27.stonks.data.remote.model.MacroIndicatorRaw
import java.time.LocalDate

private const val BUNDESBANK_BASE_URL = "https://api.statistiken.bundesbank.de/rest/data"
private const val BUND_YIELD_10Y_SERIES = "BBSIS/D.I.ZAR.ZI.EUR.S1311.B.A604.R10XX.R.A.A._Z._Z.A"
private const val GERMAN_CPI_SERIES = "BBDP1/M.DE.N.VPI.C.A00000.VGJ.LV"

private const val LOOKBACK_DAYS = 10L
private const val LOOKBACK_MONTHS = 12L

class BundesbankApi(private val httpClient: HttpClient) {

    suspend fun getGermanBundYield10Y(): MacroIndicatorRaw = fetchLatest(
        series = BUND_YIELD_10Y_SERIES,
        startPeriod = LocalDate.now().minusDays(LOOKBACK_DAYS).toString(),
    )

    suspend fun getGermanCpiYoY(): MacroIndicatorRaw = fetchLatest(
        series = GERMAN_CPI_SERIES,
        startPeriod = LocalDate.now().minusMonths(LOOKBACK_MONTHS).toString().substring(0, 7),
    )

    private suspend fun fetchLatest(series: String, startPeriod: String): MacroIndicatorRaw =
        httpClient.get("$BUNDESBANK_BASE_URL/$series") {
            header(HttpHeaders.Accept, "application/vnd.sdmx.data+json;version=1.0.0")
            parameter("startPeriod", startPeriod)
        }.body<BundesbankResponse>().toRaw()
}
