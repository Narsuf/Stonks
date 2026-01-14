package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.getString
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.home.entities.HomeState.Content
import org.n27.stonks.presentation.home.entities.HomeState.Content.BottomSheet
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.default_value
import stonks.composeapp.generated.resources.not_set
import java.math.BigDecimal

internal suspend fun Stocks.toContent() = Content(
    bottomSheet = BottomSheet(
        epsGrowthInput = BigDecimal.ZERO,
        valuationFloorInput = BigDecimal.ZERO,
    ),
    isWatchlistLoading = false,
    watchlist = items.toPresentationEntity(),
    isEndReached = nextPage == null,
    isPageLoading = false,
)

internal suspend fun List<Stock>.toPresentationEntity(): ImmutableList<Content.Item> = map { it.toPresentationEntity() }
    .toPersistentList()

private suspend fun Stock.toPresentationEntity() = Content.Item(
    icon = logo?.toImageBitmap(),
    name = companyName.substringBefore(" "),
    symbol = symbol,
    price = price?.toPrice(currency),
    targetPrice = price?.getTargetPrice(currentIntrinsicValue, currency),
    extraValue = "${valuationFloor?.toFormattedString() ?: getString(Res.string.default_value)} / " +
            (expectedEpsGrowth?.toFormattedPercentage() ?: getString(Res.string.not_set)),
)
