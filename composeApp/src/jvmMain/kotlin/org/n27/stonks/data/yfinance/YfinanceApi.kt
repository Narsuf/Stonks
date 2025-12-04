package org.n27.stonks.data.yfinance

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.n27.stonks.data.yfinance.model.StockRaw

class YfinanceApi(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {

    suspend fun getStocks(symbols: String): List<StockRaw> {
        val url = "$baseUrl/stocks"
        println("getStocks request triggered")
        val response: List<StockRaw> = httpClient.get(url) {
            parameter("symbols", symbols)
        }.body()

        return response
    }

    suspend fun getStock(symbol: String): StockRaw {
        val url = "$baseUrl/stock/$symbol"
        println("getStock request triggered")
        val response: StockRaw = httpClient.get(url).body()
        return response
    }
}
