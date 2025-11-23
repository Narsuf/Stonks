package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.data.common.model.StockRaw
import org.n27.stonks.data.search.model.SearchStockRaw

class Api(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {

    suspend fun getStocks(symbols: String): List<SearchStockRaw> {
        val url = "$baseUrl/stocks"
        println("getStocks request triggered")
        val response: List<SearchStockRaw> = httpClient.get(url) {
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
