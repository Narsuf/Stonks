package org.n27.stonks.test_data.presentation

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.detail.entities.DetailState.Content
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Cell
import stonks.composeapp.generated.resources.*

fun getDetailContent(
    symbol: String = "AAPL",
    name: String = "Apple Inc.",
    price: String = "$259.37",
    lastUpdated: String? = "10. Jan 16:55",
    cells: List<Cell> = listOf(
        getDetailContentCell(
            title = Res.string.dividend_yield,
            value = "0.40 %",
            description = Res.string.dividend_yield_description
        ),
        getDetailContentCell(
            title = Res.string.payout_ratio,
            value = "58.99 %",
            description = Res.string.payout_ratio_description
        ),
        getDetailContentCell(
            title = Res.string.intrinsic_value,
            value = "$93.38",
            description = Res.string.intrinsic_value_description,
            delta = DeltaTextEntity(
                value = "$165.99",
                percentage = "64.00 %",
                state = DeltaState.NEGATIVE
            )
        ),
        getDetailContentCell(
            title = Res.string.peg,
            value = "4.01",
            description = Res.string.peg_description,
            color = AppColors.Red,  // DANGER: > 3
        ),
        getDetailContentCell(
            title = Res.string.dynamic_payback,
            value = "16.72",
            description = Res.string.dynamic_payback_description,
            color = AppColors.Yellow,
        ),
        getDetailContentCell(
            title = Res.string.growth,
            value = "86.40 %",
            description = Res.string.growth_description
        ),
        getDetailContentCell(
            title = Res.string.earnings_estimate,
            value = "8.65 %",
            description = Res.string.earnings_estimate_description
        ),
        getDetailContentCell(
            title = Res.string.roe,
            value = "152.02 %",
            description = Res.string.roe_description,
            color = AppColors.Green,  // POSITIVE: > 15
        ),
        getDetailContentCell(
            title = Res.string.profit_margin,
            value = "27.04 %",
            description = Res.string.profit_margin_description,
            color = AppColors.Green,  // POSITIVE: > 15
        ),
        getDetailContentCell(
            title = Res.string.de,
            value = "102.63",
            description = Res.string.de_description
        ),
        getDetailContentCell(
            title = Res.string.current_ratio,
            value = "0.96",
            description = Res.string.current_ratio_description
        ),
        getDetailContentCell(
            title = Res.string.cash_to_earnings,
            value = "0.61",
            description = Res.string.cash_to_earnings_description
        ),
        getDetailContentCell(
            title = Res.string.cash_to_price,
            value = "1.76 %",
            description = Res.string.cash_to_price_description
        ),
        getDetailContentCell(
            title = Res.string.pe,
            value = "34.72",
            description = Res.string.pe_description,
            color = AppColors.Red,
        ),
        getDetailContentCell(
            title = Res.string.eps,
            value = "$7.47",
            description = Res.string.eps_description
        ),
        getDetailContentCell(
            title = Res.string.total_cash_per_share,
            value = "$4.56",
            description = Res.string.total_cash_per_share_description
        ),
    ),
    isWatchlisted: Boolean = false
) = Content(
    symbol = symbol,
    icon = null,
    name = name,
    price = price,
    lastUpdated = lastUpdated,
    cells = cells.toPersistentList(),
    isWatchlisted = isWatchlisted,
)

fun getDetailContentCell(
    title: StringResource = Res.string.dividend_yield,
    value: String = "0.40 %",
    description: StringResource = Res.string.dividend_yield_description,
    delta: DeltaTextEntity? = null,
    color: Color? = null,
) = Cell(
    title = title,
    value = value,
    description = description,
    delta = delta,
    color = color,
)
