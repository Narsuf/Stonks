package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.domain.models.Stock
import org.n27.stonks.domain.models.Stocks


class Api(baseUrl: String, private val httpClient: HttpClient) {

    private val url = baseUrl + 8080

    suspend fun getStock(symbol: String): Stock {
        val url = "$url/stock/$symbol"
        println("getStock request triggered")
        val response: Stock = httpClient.get(url).body()
        return response
    }

    suspend fun getStocks(page: Int?, symbol: String?, filterWatchlist: Boolean): Stocks {
        val url = "$url/stocks"
        println("getStocks request triggered")
        val response: Stocks = httpClient.get(url) {
            parameter("page", page)
            parameter("symbol", symbol)
            parameter("filterWatchlist", filterWatchlist)
        }.body()

        return response
    }

    suspend fun getWatchlist(page: Int?, forceUpdate: Boolean?): Stocks {
        val url = "$url/watchlist"
        println("getWatchlist request triggered")
        val response: Stocks = httpClient.get(url) {
            parameter("page", page)
            parameter("forceUpdate", forceUpdate)
        }.body()

        return response
    }

    suspend fun addToWatchlist(symbol: String) {
        val url = "$url/watchlist/$symbol"
        println("addToWatchlist request triggered for symbol: $symbol")
        httpClient.post(url)
    }

    suspend fun removeFromWatchlist(symbol: String) {
        val url = "$url/watchlist/$symbol"
        println("removeFromWatchlist request triggered for symbol: $symbol")
        httpClient.delete(url)
    }
}
