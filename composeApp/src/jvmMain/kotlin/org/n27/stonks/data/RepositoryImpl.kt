package org.n27.stonks.data

import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching { api.getStock(symbol) }

    override suspend fun getStocks(
        filterWatchlist: Boolean,
        symbol: String?,
        from: Int?,
        pageSize: Int?,
    ): Result<Stocks> = runCatching { api.getStocks(filterWatchlist, symbol, from, pageSize) }

    override suspend fun getWatchlist(
        from: Int?,
        pageSize: Int?,
    ): Result<Stocks> = runCatching { api.getWatchlist(from, pageSize) }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching { api.addToWatchlist(symbol) }

    override suspend fun removeFromWatchlist(
        symbol: String,
    ): Result<Unit> = runCatching { api.removeFromWatchlist(symbol) }

    override suspend fun editWatchlistItem(
        symbol: String,
        epsGrowth: Double,
        valuationFloor: Double?,
    ): Result<Unit> = runCatching { api.addCustomValues(symbol, epsGrowth, valuationFloor) }
}
