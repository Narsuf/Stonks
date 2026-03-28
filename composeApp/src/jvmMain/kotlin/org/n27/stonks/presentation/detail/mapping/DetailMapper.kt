package org.n27.stonks.presentation.detail.mapping

import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.domain.models.Stocks.Stock.Analysis.EarningsEstimate
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.detail.entities.DetailState.Content
import stonks.composeapp.generated.resources.*

internal fun Stock.toDetailContent() = Content(
    symbol = symbol,
    icon = logo?.toImageBitmap(),
    name = companyName.truncateAfterDoubleSpace(),
    price = price?.toPrice(currency),
    lastUpdated = lastUpdated?.toDateString(),
    cells = buildList {
        dividends?.dividendYield?.toDividendCell()?.let(::add)
        dividends?.payoutRatio?.toPayoutRatioCell()?.let(::add)
        valuationMeasures?.intrinsicValue?.toIntrinsicValueCell(this@toDetailContent)?.let(::add)
        valuationMeasures?.pe?.toPeCell()?.let(::add)
        computed?.peg?.toPegCell()?.let(::add)
        computed?.dynamicPayback?.toDynamicPaybackCell()?.let(::add)
        computed?.earningsYield?.toEarningsYieldCell()?.let(::add)
        incomeStatement?.earningsQuarterlyGrowth?.toGrowthCell()?.let(::add)
        analysis?.earningsEstimate?.toEarningsEstimateCell()?.let(::add)
        incomeStatement?.eps?.toEpsCell(currency)?.let(::add)
        profitMargin?.toProfitMarginCell()?.let(::add)
        roe?.toRoeCell()?.let(::add)
        balanceSheet?.totalCashPerShare?.toTotalCashPerShareCell(currency)?.let(::add)
        computed?.cashToEarnings?.toCashToEarningsCell()?.let(::add)
        computed?.cashToPrice?.toCashToPriceCell()?.let(::add)
        balanceSheet?.de?.toDeCell()?.let(::add)
    }.toPersistentList(),
    isWatchlisted = isWatchlisted,
)

private fun Double.toDividendCell() = toFormattedPercentage().toCell(
    title = Res.string.dividend_yield,
    description = Res.string.dividend_yield_description,
)

private fun Double.toPayoutRatioCell() = (this * 100).toFormattedPercentage().toCell(
    title = Res.string.payout_ratio,
    description = Res.string.payout_ratio_description,
)

private fun Double.toIntrinsicValueCell(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = Res.string.intrinsic_value,
    description = Res.string.intrinsic_value_description,
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

private fun Double.toDynamicPaybackCell() = toFormattedString().toCell(
    title = Res.string.dynamic_payback,
    description = Res.string.dynamic_payback_description,
)

private fun Double.toPeCell() = toFormattedString().toCell(
    title = Res.string.pe,
    description = Res.string.pe_description,
)

private fun Double.toEarningsYieldCell() = toFormattedPercentage().toCell(
    title = Res.string.earnings_yield,
    description = Res.string.earnings_yield_description,
)

private fun Double.toPegCell() = toFormattedString().toCell(
    title = Res.string.peg,
    description = Res.string.peg_description,
)

private fun Double.toGrowthCell() = toFormattedPercentage().toCell(
    title = Res.string.growth,
    description = Res.string.growth_description,
)

private fun Double.toEpsCell(currency: String?) = toPrice(currency)?.toCell(
    title = Res.string.eps,
    description = Res.string.eps_description,
)

private fun Double.toProfitMarginCell() = toFormattedPercentage().toCell(
    title = Res.string.profit_margin,
    description = Res.string.profit_margin_description,
)

private fun Double.toRoeCell() = toFormattedPercentage().toCell(
    title = Res.string.roe,
    description = Res.string.roe_description,
)

private fun Double.toTotalCashPerShareCell(currency: String?) = toPrice(currency)?.toCell(
    title = Res.string.total_cash_per_share,
    description = Res.string.total_cash_per_share_description,
)

private fun Double.toCashToEarningsCell() = toFormattedString().toCell(
    title = Res.string.cash_to_earnings,
    description = Res.string.cash_to_earnings_description,
)

private fun Double.toCashToPriceCell() = toFormattedPercentage().toCell(
    title = Res.string.cash_to_price,
    description = Res.string.cash_to_price_description,
)

private fun Double.toDeCell() = toFormattedString().toCell(
    title = Res.string.de,
    description = Res.string.de_description,
)

private fun EarningsEstimate.toEarningsEstimateCell(): Content.Cell? {
    val low = growthLow ?: return null
    val high = growthHigh ?: return null
    return "${low.toFormattedString()} - ${high.toFormattedString()} %".toCell(
        title = Res.string.earnings_estimate,
        description = Res.string.earnings_estimate_description,
    )
}

private fun String.toCell(
    title: StringResource,
    description: StringResource,
    delta: DeltaTextEntity? = null,
) = Content.Cell(
    title = title,
    value = this,
    description = description,
    delta = delta,
)
