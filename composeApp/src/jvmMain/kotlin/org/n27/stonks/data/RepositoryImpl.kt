package org.n27.stonks.data

import org.n27.stonks.PAGE_SIZE
import org.n27.stonks.data.common.mapping.toDomainEntity
import org.n27.stonks.data.json.JsonReader
import org.n27.stonks.data.json.JsonStorage
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.common.Stock
import org.n27.stonks.domain.common.Stocks
import org.n27.stonks.domain.watchlist.StockInfo
import org.n27.stonks.domain.watchlist.Watchlist

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        api.getStock(symbol).toDomainEntity()
    }

    override suspend fun getStocks(
        from: Int?,
        symbol: String?,
        filterWatchlist: Boolean,
    ): Result<Stocks> = runCatching {
        val start = from ?: 0
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
            .drop(start)
            .take(PAGE_SIZE)
            .joinToString(separator = ",")

        val nextPage = start + PAGE_SIZE
        api.getStocks(paginatedParams).toDomainEntity(
            nextPage = nextPage.takeIf { it <= filteredParams.size },
        )
    }

    override suspend fun getWatchlist(from: Int?): Result<Stocks> = runCatching {
        val watchlist = Watchlist(JsonStorage.load())
        val start = from ?: 0
        val symbols = watchlist.items
            .drop(start)
            .take(PAGE_SIZE)
            .map { it.symbol }

        val nextPage = start + PAGE_SIZE
        val formattedSymbols = symbols.joinToString(separator = ",")
        api.getStocks(formattedSymbols).toDomainEntity(nextPage = nextPage.takeIf { it <= watchlist.items.size })
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

    private suspend fun getFilteredSymbols(symbol: String): List<String> = JsonReader.getSymbols()
        .filter { it.contains(symbol) }
}
