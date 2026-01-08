package org.n27.stonks.domain

import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun getStocks(
        filterWatchlist: Boolean,
        symbol: String? = null,
        from: Int? = null,
        pageSize: Int? = null,
    ): Result<Stocks>

    suspend fun getWatchlist(from: Int? = null, pageSize: Int? = null): Result<Stocks>
    suspend fun addToWatchlist(symbol: String): Result<Unit>
    suspend fun removeFromWatchlist(symbol: String): Result<Unit>
    suspend fun editWatchlistItem(symbol: String, epsGrowth: Double, valuationFloor: Double?): Result<Unit>
}
