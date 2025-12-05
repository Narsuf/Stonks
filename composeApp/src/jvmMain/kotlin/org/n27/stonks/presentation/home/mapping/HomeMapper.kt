package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.models.Stock
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.presentation.common.extensions.getTargetPrice
import org.n27.stonks.presentation.common.extensions.toFormattedPercentage
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.home.entities.HomeState.Content
import java.math.BigDecimal

internal fun Stocks.toContent() = Content(
    input = BigDecimal.ZERO,
    isWatchlistLoading = false,
    watchlist = items.toPresentationEntity(),
    isEndReached = nextPage == null,
    isPageLoading = false,
)

internal fun List<Stock>.toPresentationEntity(): ImmutableList<Content.Item> = map { it.toPresentationEntity() }
    .toPersistentList()

private fun Stock.toPresentationEntity() = Content.Item(
    iconUrl = logoUrl ?: "",
    name = companyName.substringBefore(" "),
    symbol = symbol,
    price = price?.toPrice(currency),
    targetPrice = price?.getTargetPrice(currentIntrinsicValue, currency),
    extraValue = expectedEpsGrowth?.toFormattedPercentage(),
)
