package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.models.Stock
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.home.entities.HomeState.Content
import org.n27.stonks.presentation.home.entities.HomeState.Content.BottomSheet
import java.math.BigDecimal

internal fun Stocks.toContent() = Content(
    bottomSheet = BottomSheet(
        epsGrowthInput = BigDecimal.ZERO,
        valuationFloorInput = BigDecimal.ZERO,
    ),
    isWatchlistLoading = false,
    watchlist = items.toPresentationEntity(),
    isEndReached = nextPage == null,
    isPageLoading = false,
)

internal fun List<Stock>.toPresentationEntity(): ImmutableList<Content.Item> = map { it.toPresentationEntity() }
    .toPersistentList()

private fun Stock.toPresentationEntity() = Content.Item(
    icon = logo?.toImageBitmap(),
    name = companyName.substringBefore(" "),
    symbol = symbol,
    price = price?.toPrice(currency),
    targetPrice = price?.getTargetPrice(currentIntrinsicValue, currency),
    extraValue = "${valuationFloor?.toFormattedString() ?: "Default"} / " +
            (expectedEpsGrowth?.toFormattedPercentage()  ?: "Not set"),
)
