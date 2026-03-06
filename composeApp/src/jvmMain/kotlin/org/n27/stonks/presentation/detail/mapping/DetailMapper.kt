package org.n27.stonks.presentation.detail.mapping

import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.domain.models.Stocks.Stock.Analysis
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
        dividendYield?.toDividendCell()?.let(::add)
        toPayoutRatioCell()?.let(::add)
        valuationMeasures?.intrinsicValue?.toIntrinsicValue(this@toDetailContent)?.let(::add)
        valuationMeasures?.pe?.toPe()?.let(::add)
        valuationMeasures?.ps?.toPs()?.let(::add)
        valuationMeasures?.pb?.toPb()?.let(::add)
        incomeStatement?.revenueQuarterlyGrowth?.toRevenueGrowth()?.let(::add)
        analysis?.revenueEstimate?.toRevenueEstimateCell()?.let(::add)
        incomeStatement?.earningsQuarterlyGrowth?.toGrowth()?.let(::add)
        analysis?.earningsEstimate?.toEarningsEstimateCell()?.let(::add)
        incomeStatement?.eps?.toEpsCell(currency)?.let(::add)
    }.toPersistentList(),
    isWatchlisted = isWatchlisted,
)

private fun Double.toDividendCell() = toFormattedPercentage().toCell(
    title = Res.string.dividend_yield,
    description = Res.string.dividend_yield_description,
)

private fun Stock.toPayoutRatioCell(): Content.Cell? {
    val eps = incomeStatement?.eps

    return if (dividendYield == null || price == null || eps == null || eps == 0.0) {
        null
    } else {
        val dividendPerShare = (dividendYield / 100) * price
        val payoutRatio = (dividendPerShare / eps) * 100
        payoutRatio.toFormattedPercentage().toCell(
            title = Res.string.payout_ratio,
            description = Res.string.payout_ratio_description,
        )
    }
}

private fun Double.toEpsCell(currency: String?) = toPrice(currency)?.toCell(
    title = Res.string.eps,
    description = Res.string.eps_description,
)

private fun Double.toPs() = toFormattedString().toCell(
    title = Res.string.ps,
    description = Res.string.ps_description,
)

private fun Double.toPe() = toFormattedString().toCell(
    title = Res.string.pe,
    description = Res.string.pe_description,
)

private fun Double.toPb() = toFormattedString().toCell(
    title = Res.string.pb,
    description = Res.string.pb_description,
)

private fun Double.toGrowth() = toFormattedPercentage().toCell(
    title = Res.string.growth,
    description = Res.string.growth_description,
)

private fun Double.toRevenueGrowth() = toFormattedPercentage().toCell(
    title = Res.string.revenue_growth,
    description = Res.string.revenue_growth_description,
)

private fun Double.toIntrinsicValue(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = Res.string.intrinsic_value,
    description = Res.string.intrinsic_value_description,
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

private fun Analysis.EarningsEstimate.toEarningsEstimateCell(): Content.Cell? {
    val low = growthLow ?: return null
    val high = growthHigh ?: return null
    return "${low.toFormattedString()} - ${high.toFormattedString()} %".toCell(
        title = Res.string.earnings_estimate,
        description = Res.string.earnings_estimate_description,
    )
}

private fun Analysis.RevenueEstimate.toRevenueEstimateCell(): Content.Cell? {
    val low = growthLow ?: return null
    val high = growthHigh ?: return null
    return "${low.toFormattedString()} - ${high.toFormattedString()} %".toCell(
        title = Res.string.revenue_estimate,
        description = Res.string.revenue_estimate_description,
    )
}

private fun String.toCell(
    title: StringResource,
    description: StringResource,
    delta: DeltaTextEntity? = null
) = Content.Cell(
    title = title,
    value = this,
    description = description,
    delta = delta,
)
