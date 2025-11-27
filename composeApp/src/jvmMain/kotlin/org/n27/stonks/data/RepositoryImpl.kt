package org.n27.stonks.data

import org.n27.stonks.data.common.mapping.toDomainEntity
import org.n27.stonks.data.json.JsonReader
import org.n27.stonks.data.json.JsonStorage
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.models.common.Stock
import org.n27.stonks.domain.models.common.Stocks
import org.n27.stonks.domain.models.watchlist.StockInfo
import org.n27.stonks.domain.models.watchlist.Watchlist

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        api.getStock(symbol).toDomainEntity()
    }

    override suspend fun getStocks(symbols: List<String>): Result<Stocks> = runCatching {
        val formattedSymbols = symbols.joinToString(separator = ",")
        api.getStocks(formattedSymbols).toDomainEntity()
    }

    override suspend fun getStocks(
        from: Int,
        size: Int,
        symbol: String?,
        filterWatchlist: Boolean,
    ): Result<Stocks> = runCatching {
        val params = symbol?.takeIf { it.isNotEmpty() }
            ?.let { getFilteredSymbols(it) }
            ?: JsonReader.getSymbols()

        val filteredParams = if (filterWatchlist) {
            val watchlist = JsonStorage.load().map { it.symbol }
            params.filter { it !in watchlist }
        } else {
            params
        }

        val paginatedParams = filteredParams
            .drop(from)
            .take(size)
            .joinToString(separator = ",")

        api.getStocks(paginatedParams).toDomainEntity(
            nextPage = from + size + 1,
        )
    }

    override suspend fun getWatchlist(): Result<Watchlist> = runCatching {
        Watchlist(JsonStorage.load())
    }

    override suspend fun addToWatchlist(symbol: String): Result<Unit> = runCatching {
        val current = JsonStorage.load()
        if (current.any { it.symbol == symbol })
            throw IllegalStateException("Already existing object $symbol in stonks.json")
        JsonStorage.save(current + StockInfo(symbol))
    }

    override suspend fun removeFromWatchlist(symbol: String): Result<Unit> = runCatching {
        val current = JsonStorage.load()
        JsonStorage.save(current.filterNot { it.symbol == symbol })
    }

    override suspend fun editWatchlistItem(symbol: String, expectedEpsGrowth: Double): Result<Unit> = runCatching {
        val current = JsonStorage.load()
        val updated = current.map { stock ->
            if (stock.symbol == symbol) stock.copy(expectedEpsGrowth = expectedEpsGrowth)
            else stock
        }
        JsonStorage.save(updated)
    }

    private suspend fun getFilteredSymbols(symbol: String): List<String> = JsonReader.getSymbols()
        .filter { it.contains(symbol) }
}
