package org.n27.stonks.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.stonks.data.remote.model.StockRaw
import org.n27.stonks.data.remote.model.StocksRaw
import org.slf4j.LoggerFactory

class Api(private val url: String, private val httpClient: HttpClient) {

    private val logger = LoggerFactory.getLogger(Api::class.java)

    suspend fun getStock(symbol: String): StockRaw {
        logger.info("getStock request triggered")
        return httpClient.get("$url/stock/$symbol").body()
    }

    suspend fun getStocks(filterWatchlist: Boolean, symbol: String?, page: Int?, pageSize: Int?): StocksRaw {
        logger.info("getStocks request triggered")
        return httpClient.get("$url/stocks") {
            parameter("filterWatchlist", filterWatchlist)
            parameter("symbol", symbol)
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
    }

    suspend fun addCustomValues(symbol: String, valuationFloor: Double) {
        logger.info("addCustomValues request triggered for symbol: $symbol")
        httpClient.post("$url/stock/$symbol/valuation") { parameter("valuationFloor", valuationFloor) }
    }

    suspend fun getWatchlist(page: Int?, pageSize: Int?): StocksRaw {
        logger.info("getWatchlist request triggered")
        return httpClient.get("$url/watchlist") {
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
    }

    suspend fun addToWatchlist(symbol: String) {
        logger.info("addToWatchlist request triggered for symbol: $symbol")
        httpClient.post("$url/watchlist/$symbol")
    }

    suspend fun removeFromWatchlist(symbol: String) {
        logger.info("removeFromWatchlist request triggered for symbol: $symbol")
        httpClient.delete("$url/watchlist/$symbol")
    }
}
