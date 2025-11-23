package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.home.Home
import org.n27.stonks.domain.home.Home.Stock
import org.n27.stonks.domain.home.StockInfo
import org.n27.stonks.domain.home.Watchlist
import org.n27.stonks.presentation.common.extensions.getTargetPrice
import org.n27.stonks.presentation.common.extensions.toFormattedBigDecimal
import org.n27.stonks.presentation.common.extensions.toFormattedPercentage
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.home.entities.HomeState.Content
import java.math.BigDecimal

internal fun Home.toContent(
    watchlist: Watchlist,
) = Content(
    input = BigDecimal.ZERO,
    isWatchlistLoading = false,
    watchlist = items.toPresentationEntity(watchlist.items),
    isEndReached = items.size == watchlist.items.size,
    isPageLoading = false,
)

internal fun List<Stock>.toPresentationEntity(
    watchlist: List<StockInfo>? = null,
): ImmutableList<Content.Item> = mapIndexed { index, stock ->
    watchlist?.get(index)
        ?.let { stock.toPresentationEntity(it) }
        ?: stock.toPresentationEntity()
}.toPersistentList()

private fun Stock.toPresentationEntity(stockInfo: StockInfo? = null) = Content.Item(
    iconUrl = logoUrl ?: "",
    name = companyName.substringBefore(" "),
    symbol = symbol,
    price = price?.toFormattedBigDecimal()?.toPrice(currency),
    targetPrice = price?.getTargetPrice(eps, stockInfo?.expectedEpsGrowth),
    estimatedEpsGrowth = stockInfo?.expectedEpsGrowth?.toFormattedPercentage(),
)
