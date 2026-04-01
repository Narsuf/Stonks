package org.n27.stonks.test_data.presentation

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.mockito.kotlin.description
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg.Text
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
            first = getDetailContentCell(
                title = Res.string.dividend_yield,
                value = "0.40 %",
                description = Res.string.dividend_yield_description,
            ),
            second = getDetailContentCell(
                title = Res.string.payout_ratio,
                value = "58.99 %",
                description = Res.string.payout_ratio_description,
            ),
        ),
        Item.Header(Res.string.section_valuation),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.eps,
                value = "$7.47",
                description = Res.string.eps_description,
            ),
            second = getDetailContentCell(
                title = Res.string.pe,
                value = "34.72",
                description = Res.string.pe_description,
                color = AppColors.Red,
            ),
        ),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.intrinsic_value,
                value = "$93.38",
                description = Res.string.intrinsic_value_description,
                delta = DeltaTextEntity(value = "$165.99", percentage = "64.00 %", state = DeltaState.NEGATIVE),
            ),
            second = getDetailContentCell(
                title = Res.string.ey_treasury_spread,
                value = "1.38 %",
                description = Res.string.ey_treasury_spread_description,
                color = AppColors.Yellow,
            ),
        ),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.peg,
                value = "3.04",
                description = Res.string.peg_description,
                color = AppColors.Red,
            ),
            second = getDetailContentCell(
                title = Res.string.dynamic_payback,
                value = "14.81",
                description = Res.string.dynamic_payback_description,
            ),
        ),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.growth,
                value = "86.40 %",
                description = Res.string.growth_description,
            ),
            second = getDetailContentCell(
                title = Res.string.earnings_estimate,
                value = "11.43 %",
                description = Res.string.earnings_estimate_description
            ),
        ),
        Item.Header(Res.string.section_fundamentals),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.roe,
                value = "1.52 %",
                description = Res.string.roe_description,
                color = AppColors.Yellow,
            ),
            second = getDetailContentCell(
                title = Res.string.profit_margin,
                value = "0.27 %",
                description = Res.string.profit_margin_description,
                color = AppColors.Yellow,
            ),
        ),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.de,
                value = "102.63",
                description = Res.string.de_description,
                color = AppColors.Red,
            ),
            second = getDetailContentCell(
                title = Res.string.current_ratio,
                value = "0.96",
                description = Res.string.current_ratio_description,
            ),
        ),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.total_cash_per_share,
                value = "$4.56",
                description = Res.string.total_cash_per_share_description,
            ),
            second = getDetailContentCell(
                title = Res.string.cash_to_earnings,
                value = "0.61",
                description = Res.string.cash_to_earnings_description,
                color = AppColors.Yellow,
            ),
        ),
        Item.Header(Res.string.section_bond_yields),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.treasury_10y_us,
                value = "1.50 %",
                description = Res.string.treasury_10y_us_description,
                descriptionArgs = persistentListOf(Text("30 Mar 2026")),
            ),
            second = getDetailContentCell(
                title = Res.string.treasury_10y_eu,
                value = "1.50 %",
                description = Res.string.treasury_10y_eu_description,
                descriptionArgs = persistentListOf(Text("Jan 2026")),
            ),
        ),
        Item.CellPair(
            first = getDetailContentCell(
                title = Res.string.german_cpi,
                value = "1.90 %",
                description = Res.string.german_cpi_description,
                descriptionArgs = persistentListOf(Text("Dec 2025")),
            ),
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
    descriptionArgs: ImmutableList<Arg> = persistentListOf(),
    delta: DeltaTextEntity? = null,
    color: Color? = null,
) = Cell(
    title = title,
    value = value,
    description = StringResourceWithArgs(
        resource = description,
        args = descriptionArgs,
    ),
    delta = delta,
    color = color,
)
