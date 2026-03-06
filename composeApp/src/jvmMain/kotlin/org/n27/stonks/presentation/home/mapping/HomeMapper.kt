package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg.Resource
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg.Text
import org.n27.stonks.presentation.common.extensions.getTargetPrice
import org.n27.stonks.presentation.common.extensions.toFormattedString
import org.n27.stonks.presentation.common.extensions.toImageBitmap
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.home.entities.HomeState.Content
import org.n27.stonks.presentation.home.entities.HomeState.Content.BottomSheet
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.default_value
import stonks.composeapp.generated.resources.valuation_and_growth
import java.math.BigDecimal

internal fun Stocks.toContent() = Content(
    bottomSheet = BottomSheet(
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
    targetPrice = price?.getTargetPrice(valuationMeasures?.intrinsicValue, currency),
    extraValue = StringResourceWithArgs(
        resource = Res.string.valuation_and_growth,
        args = persistentListOf(
            valuationMeasures?.valuationFloor?.toFormattedString()
                ?.let { Text(it) }
                ?: Resource(Res.string.default_value),
        ),
    ),
)
