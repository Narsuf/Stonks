package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.models.common.Stock
import org.n27.stonks.domain.models.common.Stocks
import org.n27.stonks.domain.models.watchlist.StockInfo
import org.n27.stonks.domain.models.watchlist.Watchlist
import org.n27.stonks.presentation.common.extensions.getTargetPrice
import org.n27.stonks.presentation.common.extensions.toFormattedBigDecimal
import org.n27.stonks.presentation.common.extensions.toFormattedPercentage
import org.n27.stonks.presentation.common.extensions.toIntrinsicValue
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.home.entities.HomeState.Content
import java.math.BigDecimal

internal fun Stocks.toContent(
    watchlist: Watchlist,
) = Content(
    input = BigDecimal.ZERO,
    isWatchlistLoading = false,
    watchlist = items.toPresentationEntity(watchlist.items),
    isEndReached = items.size == watchlist.items.size,
    isPageLoading = false,
)

private fun List<Stock>.toPresentationEntity(
    watchlist: List<StockInfo>,
): ImmutableList<Content.Item> = mapIndexed { index, stock ->
    stock.toPresentationEntity(  watchlist[index])
}.toPersistentList()

private fun Stock.toPresentationEntity(stockInfo: StockInfo) = Content.Item(
    iconUrl = logoUrl ?: "",
    name = companyName.substringBefore(" "),
    symbol = symbol,
    price = price?.toPrice(currency),
    targetPrice = price?.getTargetPrice(currentIntrinsicValue, currency),
    estimatedEpsGrowth = stockInfo.expectedEpsGrowth?.toFormattedPercentage(),
)
