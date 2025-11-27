package org.n27.stonks.domain

import org.n27.stonks.domain.models.common.Stock
import org.n27.stonks.domain.models.common.Stocks
import org.n27.stonks.domain.models.watchlist.Watchlist

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun getStocks(symbols: List<String>): Result<Stocks>
    suspend fun getStocks(
        from: Int,
        size: Int,
        symbol: String? = null,
        filterWatchlist: Boolean = false,
    ): Result<Stocks>

    suspend fun getWatchlist(): Result<Watchlist>
    suspend fun addToWatchlist(symbol: String): Result<Unit>
    suspend fun editWatchlistItem(symbol: String, expectedEpsGrowth: Double): Result<Unit>
    suspend fun removeFromWatchlist(symbol: String): Result<Unit>
}
