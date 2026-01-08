package org.n27.stonks.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock


class Api(private val url: String, private val httpClient: HttpClient) {

    suspend fun getStock(symbol: String): Stock {
        val url = "$url/stock/$symbol"
        println("getStock request triggered")
        val response: Stock = httpClient.get(url).body()
        return response
    }

    suspend fun addCustomValues(symbol: String, epsGrowth: Double, valuationFloor: Double?) {
        val url = "$url/stock/$symbol/valuation"
        println("addCustomValues request triggered for symbol: $symbol")
        httpClient.post(url) {
            parameter("epsGrowth", epsGrowth)
            parameter("valuationFloor", valuationFloor)
        }
    }

    suspend fun getStocks(filterWatchlist: Boolean, symbol: String?, page: Int?, pageSize: Int?): Stocks {
        val url = "$url/stocks"
        println("getStocks request triggered")
        val response: Stocks = httpClient.get(url) {
            parameter("filterWatchlist", filterWatchlist)
            parameter("symbol", symbol)
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()

        return response
    }

    suspend fun getWatchlist(page: Int?, pageSize: Int?): Stocks {
        val url = "$url/watchlist"
        println("getWatchlist request triggered")
        val response: Stocks = httpClient.get(url) {
            parameter("page", page)
            parameter("pageSize", pageSize)
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
