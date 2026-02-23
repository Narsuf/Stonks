package org.n27.stonks.presentation.detail.mapping

import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.domain.models.Stocks.Stock
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
        earningsQuarterlyGrowth?.toGrowth()?.let(::add)
        expectedEpsGrowth?.toExpectedEpsGrowth()?.let(::add)
        currentIntrinsicValue?.toIntrinsicValue(this@toDetailContent)?.let(::add)
        forwardIntrinsicValue?.toForwardIntrinsicValue(this@toDetailContent)?.let(::add)
        pe?.toPe()?.let(::add)
        pb?.toPb()?.let(::add)
        eps?.toEpsCell(currency)?.let(::add)
    }.toPersistentList(),
)

private fun Double.toDividendCell() = toFormattedPercentage().toCell(
    title = Res.string.dividend_yield,
    description = Res.string.dividend_yield_description,
)

private fun Stock.toPayoutRatioCell() = if (dividendYield == null || price == null || eps == null || eps == 0.0) {
    null
} else {
    val dividendPerShare = (dividendYield / 100) * price
    val payoutRatio = (dividendPerShare / eps) * 100
    payoutRatio.toFormattedPercentage().toCell(
        title = Res.string.payout_ratio,
        description = Res.string.payout_ratio_description,
    )
}

private fun Double.toEpsCell(currency: String?) = toPrice(currency)?.toCell(
    title = Res.string.eps,
    description = Res.string.eps_description,
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

private fun Double.toExpectedEpsGrowth() = toFormattedPercentage().toCell(
    title = Res.string.forward_growth,
    description = Res.string.forward_growth_description,
)

private fun Double.toIntrinsicValue(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = Res.string.intrinsic_value,
    description = Res.string.intrinsic_value_description,
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

private fun Double.toForwardIntrinsicValue(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = Res.string.forward_intrinsic_value,
    description = Res.string.forward_intrinsic_value_description,
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

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
