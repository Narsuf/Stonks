package org.n27.stonks.presentation.detail.mapping

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.domain.models.MacroIndicators
import org.n27.stonks.domain.models.MacroIndicators.MacroIndicator
import org.n27.stonks.domain.models.RatedValue
import org.n27.stonks.domain.models.Rating
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.domain.models.Stocks.Stock.EarningsEstimate
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.common.mapping.toColor
import org.n27.stonks.presentation.detail.entities.DetailState.Content
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Cell
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Item
import stonks.composeapp.generated.resources.*

internal fun Stock.toDetailContent(indicators: MacroIndicators? = null) = Content(
    symbol = symbol,
    icon = logo?.toImageBitmap(),
    name = companyName.truncateAfterDoubleSpace(),
    price = price?.toPrice(currency),
    lastUpdated = lastUpdated?.toDateString(),
    items = buildList {
        fun addSection(title: StringResource, build: MutableList<Item>.() -> Unit) {
            val items = buildList(build)
            if (items.isEmpty()) return
            add(Item.Header(title))
            addAll(items)
        }

        fun MutableList<Item>.addPair(first: Cell?, second: Cell? = null) {
            first?.let { add(Item.CellPair(it, second)) }
        }

        addSection(Res.string.section_dividends) {
            addPair(
                first = dividends?.dividendYield?.toDividendCell(),
                second = dividends?.payoutRatio?.toPayoutRatioCell(),
            )
        }

        addSection(Res.string.section_valuation) {
            addPair(
                first = incomeStatement?.eps?.toEpsCell(currency),
                second = valuationMeasures?.pe?.toPeCell(),
            )
            addPair(
                first = valuationMeasures?.intrinsicValue?.toIntrinsicValueCell(this@toDetailContent),
                second = computeEyTreasurySpread(computed?.earningsYield, indicators?.treasury10Y?.value)?.toEyTreasurySpreadCell(),
            )
            addPair(
                first = computed?.peg?.toPegCell(),
                second = computed?.dynamicPayback?.toDynamicPaybackCell(),
            )
            addPair(
                first = incomeStatement?.earningsQuarterlyGrowth?.toGrowthCell(),
                second = earningsEstimate?.toEarningsEstimateCell(),
            )
        }

        addSection(Res.string.section_fundamentals) {
            addPair(
                first = roe?.toRoeCell(),
                second = profitMargin?.toProfitMarginCell(),
            )
            addPair(
                first = balanceSheet?.de?.toDeCell(),
                second = balanceSheet?.currentRatio?.toCurrentRatioCell(),
            )
            addPair(
                first = balanceSheet?.totalCashPerShare?.toTotalCashPerShareCell(currency),
                second = computed?.cashToEarnings?.toCashToEarningsCell(),
            )
        }

        addSection(Res.string.section_bond_yields) {
            addPair(
                first = indicators?.treasury10Y?.toUsTreasuryCell(),
                second = indicators?.europeanTreasury10Y?.toEuTreasuryCell(),
            )
            addPair(
                first = indicators?.germanCpi?.toGermanCpiCell(),
            )
        }
    }.toPersistentList(),
    isWatchlisted = isWatchlisted,
)

private fun computeEyTreasurySpread(earningsYield: Double?, treasury10Y: Double?) =
    if (earningsYield != null && treasury10Y != null) earningsYield - treasury10Y else null

private fun Double.toEyTreasurySpreadCell() = toFormattedPercentage().toCell(
    title = Res.string.ey_treasury_spread,
    description = StringResourceWithArgs(Res.string.ey_treasury_spread_description),
    color = takeIf { it < 2.5 }?.let { AppColors.Yellow },
)

private fun MacroIndicator.toUsTreasuryCell() = value.toFormattedPercentage().toCell(
    title = Res.string.treasury_10y_us,
    description = StringResourceWithArgs(Res.string.treasury_10y_us_description, persistentListOf(Arg.Text(date.toFormattedDate()))),
)

private fun MacroIndicator.toEuTreasuryCell() = value.toFormattedPercentage().toCell(
    title = Res.string.treasury_10y_eu,
    description = StringResourceWithArgs(Res.string.treasury_10y_eu_description, persistentListOf(Arg.Text(date.toFormattedDate()))),
)

private fun MacroIndicator.toGermanCpiCell() = value.toFormattedPercentage().toCell(
    title = Res.string.german_cpi,
    description = StringResourceWithArgs(Res.string.german_cpi_description, persistentListOf(Arg.Text(date.toFormattedDate()))),
)

private fun Double.toDividendCell() = toFormattedPercentage().toCell(
    title = Res.string.dividend_yield,
    description = StringResourceWithArgs(Res.string.dividend_yield_description),
)

private fun Double.toPayoutRatioCell() = (this * 100).toFormattedPercentage().toCell(
    title = Res.string.payout_ratio,
    description = StringResourceWithArgs(Res.string.payout_ratio_description),
)

private fun Double.toIntrinsicValueCell(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = Res.string.intrinsic_value,
    description = StringResourceWithArgs(Res.string.intrinsic_value_description),
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

private fun RatedValue.toDynamicPaybackCell() = value.toFormattedString().toCell(
    title = Res.string.dynamic_payback,
    description = StringResourceWithArgs(Res.string.dynamic_payback_description),
    color = rating?.toColor(),
)

private fun RatedValue.toPeCell() = value.toFormattedString().toCell(
    title = Res.string.pe,
    description = StringResourceWithArgs(Res.string.pe_description),
    color = rating?.toColor(),
)

private fun RatedValue.toPegCell() = value.toFormattedString().toCell(
    title = Res.string.peg,
    description = StringResourceWithArgs(Res.string.peg_description),
    color = rating?.toColor(),
)

private fun Double.toGrowthCell() = toFormattedPercentage().toCell(
    title = Res.string.growth,
    description = StringResourceWithArgs(Res.string.growth_description),
)

private fun Double.toEpsCell(currency: String?) = toPrice(currency)?.toCell(
    title = Res.string.eps,
    description = StringResourceWithArgs(Res.string.eps_description),
)

private fun RatedValue.toProfitMarginCell() = value.toFormattedPercentage().toCell(
    title = Res.string.profit_margin,
    description = StringResourceWithArgs(Res.string.profit_margin_description),
    color = rating?.toColor(),
)

private fun RatedValue.toRoeCell() = value.toFormattedPercentage().toCell(
    title = Res.string.roe,
    description = StringResourceWithArgs(Res.string.roe_description),
    color = rating?.toColor(),
)

private fun Double.toTotalCashPerShareCell(currency: String?) = toPrice(currency)?.toCell(
    title = Res.string.total_cash_per_share,
    description = StringResourceWithArgs(Res.string.total_cash_per_share_description),
)

private fun RatedValue.toCashToEarningsCell() = value.toFormattedString().toCell(
    title = Res.string.cash_to_earnings,
    description = StringResourceWithArgs(Res.string.cash_to_earnings_description),
    color = rating?.toColor(),
)

private fun RatedValue.toDeCell() = value.toFormattedString().toCell(
    title = Res.string.de,
    description = StringResourceWithArgs(Res.string.de_description),
    color = rating?.toColor(),
)

private fun EarningsEstimate.toEarningsEstimateCell() = growthHigh
    ?.toFormattedPercentage()
    ?.toCell(
        title = Res.string.earnings_estimate,
        description = StringResourceWithArgs(Res.string.earnings_estimate_description),
    )

private fun RatedValue.toCurrentRatioCell() = value.toFormattedString().toCell(
    title = Res.string.current_ratio,
    description = StringResourceWithArgs(Res.string.current_ratio_description),
    color = rating?.toColor(),
)

private fun String.toCell(
    title: StringResource,
    description: StringResourceWithArgs,
    delta: DeltaTextEntity? = null,
    color: Color? = null,
) = Cell(
    title = title,
    value = this,
    description = description,
    delta = delta,
    color = color,
)
