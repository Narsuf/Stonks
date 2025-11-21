package org.n27.stonks.domain

import org.n27.stonks.domain.common.Stock
import org.n27.stonks.domain.home.Home
import org.n27.stonks.domain.home.Watchlist
import org.n27.stonks.domain.search.Search

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun getStocks(symbols: List<String>): Result<Home>
    suspend fun getStocks(
        from: Int,
        size: Int,
        symbol: String? = null,
        filterWatchlist: Boolean = false,
    ): Result<Search>

    suspend fun getWatchlist(): Result<Watchlist>
    suspend fun addToWatchlist(symbol: String): Result<Unit>
    suspend fun editWatchlistItem(symbol: String, expectedEpsGrowth: Double): Result<Unit>
    suspend fun removeFromWatchlist(symbol: String): Result<Unit>
}
