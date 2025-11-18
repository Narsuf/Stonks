package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.data.model.StockRaw

class Api(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {

    suspend fun getStocks(from: Int, size: Int): List<StockRaw> {
        val symbolsParam = JsonReader.getSymbolsPage(from, size)?.joinToString(separator = ",")
        val url = "$baseUrl/stocks"
        val response: List<StockRaw> = httpClient.get(url) {
            parameter("symbols", symbolsParam)
        }.body()
        return response
    }

    suspend fun getStock(symbol: String): StockRaw {
        val url = "$baseUrl/stock/$symbol"
        val response: StockRaw = httpClient.get(url).body()
        return response
    }
}
