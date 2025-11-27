package org.n27.stonks.domain.watchlist

import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.common.Stocks
import org.n27.stonks.domain.watchlist.models.StockInfo
import org.n27.stonks.presentation.common.broadcast.Event.ShowErrorNotification
import org.n27.stonks.presentation.common.extensions.toIntrinsicValue
import org.n27.stonks.presentation.home.entities.HomeState
import org.n27.stonks.presentation.home.mapping.toContent
import kotlin.Result.Companion.failure
import kotlin.collections.plus
import kotlin.math.exp

private const val PAGE_SIZE = 11

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
                stocks.copy(
                    nextPage = nextPage.takeIf { it <= watchlist.items.size },
                    items = stocks.items.map { stock ->
                        val expectedEpsGrowth = watchlist.items.first { stock.symbol == it.symbol }.expectedEpsGrowth
                        stock.copy(
                            expectedEpsGrowth = expectedEpsGrowth,
                            currentIntrinsicValue = expectedEpsGrowth?.let { stock.eps?.toIntrinsicValue(it) }
                        )
                    }
                )
            }
        },
        onFailure = { failure(it) }
    )

    suspend fun addToWatchlist(symbol: String) = repository.addToWatchlist(symbol)

    suspend fun removeFromWatchlist(symbol: String) = repository.removeFromWatchlist(symbol)

    suspend fun editWatchlistItem(
        symbol: String,
        expectedEpsGrowth: Double,
    ) = repository.editWatchlistItem(symbol, expectedEpsGrowth)
}
