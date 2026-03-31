package org.n27.stonks.test_data.presentation

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.detail.entities.DetailState.Content
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Cell
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Item
import stonks.composeapp.generated.resources.*

fun getDetailContent(
    symbol: String = "AAPL",
    name: String = "Apple Inc.",
    price: String = "$259.37",
    lastUpdated: String? = "10. Jan 16:55",
    items: List<Item> = listOf(
        Item.Header(Res.string.section_dividends),
        Item.CellPair(
            first = getDetailContentCell(Res.string.dividend_yield, "0.40 %", Res.string.dividend_yield_description),
            second = getDetailContentCell(Res.string.payout_ratio, "58.99 %", Res.string.payout_ratio_description),
        ),
        Item.Header(Res.string.section_valuation),
        Item.CellPair(
            first = getDetailContentCell(Res.string.eps, "$7.47", Res.string.eps_description),
            second = getDetailContentCell(Res.string.pe, "34.72", Res.string.pe_description, color = AppColors.Red),
        ),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.intrinsic_value,
                value = "$93.38",
                description = Res.string.intrinsic_value_description,
                delta = DeltaTextEntity(value = "$165.99", percentage = "64.00 %", state = DeltaState.NEGATIVE),
            ),
            second = getDetailContentCell(Res.string.ey_treasury_spread, "1.38 %", Res.string.ey_treasury_spread_description, color = AppColors.Yellow),
        ),
        Item.CellPair(
            first = getDetailContentCell(Res.string.peg, "3.04", Res.string.peg_description, color = AppColors.Red),
            second = getDetailContentCell(Res.string.dynamic_payback, "14.81", Res.string.dynamic_payback_description, color = null),
        ),
        Item.CellPair(
            first = getDetailContentCell(Res.string.growth, "86.40 %", Res.string.growth_description),
            second = getDetailContentCell(Res.string.earnings_estimate, "11.43 %", Res.string.earnings_estimate_description),
        ),
        Item.Header(Res.string.section_fundamentals),
        Item.CellPair(
            first = getDetailContentCell(Res.string.roe, "1.52 %", Res.string.roe_description, color = AppColors.Yellow),
            second = getDetailContentCell(Res.string.profit_margin, "0.27 %", Res.string.profit_margin_description, color = AppColors.Yellow),
        ),
        Item.CellPair(
            first = getDetailContentCell(Res.string.de, "102.63", Res.string.de_description, color = AppColors.Red),
            second = getDetailContentCell(Res.string.current_ratio, "0.96", Res.string.current_ratio_description),
        ),
        Item.CellPair(
            first = getDetailContentCell(Res.string.total_cash_per_share, "$4.56", Res.string.total_cash_per_share_description),
            second = getDetailContentCell(Res.string.cash_to_earnings, "0.61", Res.string.cash_to_earnings_description, color = AppColors.Yellow),
        ),
        Item.Header(Res.string.section_bond_yields),
        Item.CellPair(
            first = getDetailContentCell(Res.string.treasury_10y_us, "1.50 %", Res.string.treasury_10y_us_description),
            second = getDetailContentCell(Res.string.treasury_10y_eu, "2.50 %", Res.string.treasury_10y_eu_description),
        ),
    ),
    isWatchlisted: Boolean = false,
) = Content(
    symbol = symbol,
    icon = null,
    name = name,
    price = price,
    lastUpdated = lastUpdated,
    items = items.toPersistentList(),
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
