package org.n27.stonks.data.yfinance

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.data.yfinance.model.StockRaw

class YfinanceApi(baseUrl: String, private val httpClient: HttpClient) {

    private val url = baseUrl + 8000

    suspend fun getStock(symbol: String): StockRaw {
        val url = "$url/stock/$symbol"
        println("getStock request triggered")
        val response: StockRaw = httpClient.get(url).body()
        return response
    }

    suspend fun getStocks(symbols: String): List<StockRaw> {
        val url = "$url/stocks"
        println("getStocks request triggered")
        val response: List<StockRaw> = httpClient.get(url) {
            parameter("symbols", symbols)
        }.body()

        return response
    }
}
