package org.n27.stonks.domain.watchlist

import org.n27.stonks.PAGE_SIZE
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.common.Stocks
import kotlin.Result.Companion.failure

class WatchlistUseCase(private val repository: Repository) {

    suspend fun getWatchlist(from: Int? = null): Result<Stocks> = repository.getWatchlist().fold(
        onSuccess = { watchlist ->
            val start = from ?: 0
            val symbols = watchlist.items
                .drop(start)
                .take(PAGE_SIZE)
                .map { it.symbol }

            val nextPage = start + PAGE_SIZE
            repository.getStocks(symbols).map { stocks ->
                stocks.copy(nextPage = nextPage.takeIf { it <= watchlist.items.size })
            }
        },
        onFailure = { failure(it) }
    )

    suspend fun addToWatchlist(symbol: String) = repository.addToWatchlist(symbol)

    suspend fun removeFromWatchlist(symbol: String) = repository.removeFromWatchlist(symbol)
}
