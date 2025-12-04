package org.n27.stonks.data

import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.common.Stock
import org.n27.stonks.domain.common.Stocks

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching { api.getStock(symbol) }

    override suspend fun getStocks(
        from: Int?,
        symbol: String?,
        filterWatchlist: Boolean,
    ): Result<Stocks> = runCatching { Stocks(api.getStocks(from, symbol, filterWatchlist)) }

    override suspend fun getWatchlist(
        from: Int?,
    ): Result<Stocks> = runCatching { Stocks(api.getWatchlist(from)) }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching {}

    override suspend fun removeFromWatchlist(symbol: String): Result<Unit> = runCatching {}
}
