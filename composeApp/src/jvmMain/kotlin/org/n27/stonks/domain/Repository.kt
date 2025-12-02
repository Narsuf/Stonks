package org.n27.stonks.domain

import org.n27.stonks.domain.common.Stock
import org.n27.stonks.domain.common.Stocks

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun getStocks(
        from: Int? = null,
        symbol: String? = null,
        filterWatchlist: Boolean = false,
    ): Result<Stocks>

    suspend fun getWatchlist(from: Int? = null): Result<Stocks>
    suspend fun addToWatchlist(symbol: String): Result<Unit>
    suspend fun removeFromWatchlist(symbol: String): Result<Unit>
}
