package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.home.Home
import org.n27.stonks.domain.home.Home.Stock
import org.n27.stonks.domain.home.StockInfo
import org.n27.stonks.domain.home.Watchlist
import org.n27.stonks.presentation.common.extensions.toFormattedBigDecimal
import org.n27.stonks.presentation.common.extensions.toFormattedPercentage
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.common.extensions.truncateAfterDoubleSpace
import org.n27.stonks.presentation.home.entities.HomeState.Content
import java.math.BigDecimal

internal fun Home.toContent(
    watchlist: Watchlist,
    home: Home,
) = Content(
    input = BigDecimal.ZERO,
    isWatchlistLoading = false,
    watchlist = items.toPresentationEntity(watchlist.items),
    isEndReached = watchlist.items.size == home.items.size,
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
    name = companyName.truncateAfterDoubleSpace(),
    symbol = symbol,
    price = price?.toFormattedBigDecimal()?.toPrice(currency),
    estimatedEpsGrowth = stockInfo?.expectedEpsGrowth?.toFormattedPercentage(),
)
