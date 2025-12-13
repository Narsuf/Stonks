package org.n27.stonks.data.yfinance

import org.n27.stonks.PAGE_SIZE
import org.n27.stonks.data.json.JsonReader
import org.n27.stonks.data.json.JsonStorage
import org.n27.stonks.data.watchlist.StockInfo
import org.n27.stonks.data.watchlist.Watchlist
import org.n27.stonks.data.yfinance.mapping.toDomainEntity
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.models.Stock
import org.n27.stonks.domain.models.Stocks

class YfinanceRepositoryImpl(private val api: YfinanceApi) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        val watchlist = Watchlist(JsonStorage.load())
        val watchlistStock = watchlist.items.firstOrNull { it.symbol == symbol }
        api.getStock(symbol).toDomainEntity(watchlistStock)
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

    override suspend fun getWatchlist(from: Int?, forceUpdate: Boolean?): Result<Stocks> = runCatching {
        val watchlist = Watchlist(JsonStorage.load())
        val start = from ?: 0
        val paginatedWatchlist = watchlist.items
            .drop(start)
            .take(PAGE_SIZE)

        val nextPage = start + PAGE_SIZE
        val formattedSymbols = paginatedWatchlist.joinToString(separator = ",") { it.symbol }
        api.getStocks(formattedSymbols).toDomainEntity(
            watchlist = paginatedWatchlist,
            nextPage = nextPage.takeIf { it <= watchlist.items.size },
        )
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

    override suspend fun editWatchlistItem(
        symbol: String,
        epsGrowth: Double,
        valuationFloor: Double,
    ): Result<Unit> = runCatching {
        val current = JsonStorage.load()
        val updated = current.map { stock ->
            if (stock.symbol == symbol) {
                stock.copy(
                    expectedEpsGrowth = epsGrowth,
                    valuationFloor = valuationFloor,
                )
            } else stock
        }
        JsonStorage.save(updated)
    }

    private suspend fun getFilteredSymbols(symbol: String): List<String> = JsonReader.getSymbols()
        .filter { it.contains(symbol) }
}
