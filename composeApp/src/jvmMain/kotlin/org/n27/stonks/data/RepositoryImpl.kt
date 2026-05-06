package org.n27.stonks.data

import org.n27.stonks.data.mapping.toDomain
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.model.Stocks
import org.n27.stonks.domain.model.Stocks.Stock

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching { api.getStock(symbol).toDomain() }

    override suspend fun getStocks(
        filterWatchlist: Boolean,
        symbol: String?,
        from: Int?,
        pageSize: Int?,
    ): Result<Stocks> = runCatching { api.getStocks(filterWatchlist, symbol, from, pageSize).toDomain() }

    override suspend fun getWatchlist(
        from: Int?,
        pageSize: Int?,
    ): Result<Stocks> = runCatching { api.getWatchlist(from, pageSize).toDomain() }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching { api.addToWatchlist(symbol) }

    override suspend fun removeFromWatchlist(
        symbol: String,
    ): Result<Unit> = runCatching { api.removeFromWatchlist(symbol) }

    override suspend fun editWatchlistItem(
        symbol: String,
        valuationFloor: Double,
    ): Result<Unit> = runCatching { api.addCustomValues(symbol, valuationFloor) }
}
