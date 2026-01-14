package org.n27.stonks.test_data.presentation

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg
import org.n27.stonks.presentation.home.entities.HomeState.Content
import org.n27.stonks.presentation.home.entities.HomeState.Content.BottomSheet
import org.n27.stonks.presentation.home.entities.HomeState.Content.Item
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.valuation_and_growth
import java.math.BigDecimal

internal fun getHomeContent(
    bottomSheet: BottomSheet = getHomeBottomSheet(),
    isWatchlistLoading: Boolean = false,
    watchlist: List<Item> = listOf(getHomeItem()),
    isEndReached: Boolean = false,
    isPageLoading: Boolean = false,
) = Content(
    bottomSheet = bottomSheet,
    isWatchlistLoading = isWatchlistLoading,
    watchlist = watchlist.toPersistentList(),
    isEndReached = isEndReached,
    isPageLoading = isPageLoading,
)

internal fun getHomeBottomSheet(
    epsGrowthInput: BigDecimal = BigDecimal.ZERO,
    valuationFloorInput: BigDecimal = BigDecimal.ZERO,
) = BottomSheet(
    epsGrowthInput = epsGrowthInput,
    valuationFloorInput = valuationFloorInput,
)

internal fun getHomeItem(
    name: String = "Apple",
    symbol: String = "AAPL",
    price: String? = "$259.37",
    targetPrice: DeltaTextEntity? = DeltaTextEntity(
        value = "$165.99",
        percentage = "64.00 %",
        state = DeltaState.NEGATIVE
    ),
    extraValue: StringResourceWithArgs? = StringResourceWithArgs(
        resource = Res.string.valuation_and_growth,
        args = persistentListOf<Arg>(
            Arg.Text("12.50"),
            Arg.Text("7.72 %"),
        )
    ),
) = Item(
    icon = null,
    name = name,
    symbol = symbol,
    price = price,
    targetPrice = targetPrice,
    extraValue = extraValue,
)

