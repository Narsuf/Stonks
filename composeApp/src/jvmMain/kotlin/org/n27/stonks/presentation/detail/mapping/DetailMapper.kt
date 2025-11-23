package org.n27.stonks.presentation.detail.mapping

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.common.Stock
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.detail.entities.DetailState.Content

internal fun Stock.toDetailContent(expectedEpsGrowth: Double?) = Content(
    symbol = symbol,
    logoUrl = logoUrl ?: "",
    name = companyName.truncateAfterDoubleSpace(),
    price = price?.toFormattedBigDecimal()?.toPrice(currency),
    targetPrice = price?.getTargetPrice(eps, expectedEpsGrowth, currency),
    cells = buildList {
        dividendYield?.toDividendCell()?.let(::add)
        eps?.toEpsCell(currency)?.let(::add)
        trailingPe?.toTrailingPe()?.let(::add)
        forwardPe?.toForwardPe()?.let(::add)
        earningsQuarterlyGrowth?.toGrowth()?.let(::add)
        intrinsicValue?.toIntrinsicValue(currency)?.let(::add)
    }.toPersistentList(),
)

private fun Double.toDividendCell() = toFormattedPercentage().toCell(
    title = "Dividend yield",
    description = "How much a company pays in dividends each year relative to its stock price.",
)

private fun Double.toEpsCell(currency: String?) = toFormattedBigDecimal().toPrice(currency)?.toCell(
    title = "EPS",
    description = "The company’s profit divided by the total number of shares.",
)

private fun Double.toTrailingPe() = toFormattedString().toCell(
    title = "Trailing P/E",
    description = "The price of a stock divided by its earnings per share over the past 12 months.\n\n" +
            "A P/E around 15 is usually seen as a reasonable value.",
)

private fun Double.toForwardPe() = toFormattedString().toCell(
    title = "Forward P/E",
    description = "The price of a stock divided by the expected earnings per share for the next 12 months.\n\n" +
            "A P/E around 15 is usually seen as a reasonable value.",
)

private fun Double.toGrowth() = toFormattedPercentage().toCell(
    title = "Growth",
    description = "Quarterly Earnings Growth (yoy).\n\nThe growth compared to the same quarter of the previous year.",
)

private fun Double.toIntrinsicValue(currency: String?) = toFormattedBigDecimal().toPrice(currency)?.toCell(
    title = "Intrinsic Value",
    description = "(Experimental) Estimated intrinsic value calculated with the Quarterly Earnings Growth.\n\n" +
            "Guessing that the growth of the following year, will be similar to the growth of the previous year.",
)

private fun String.toCell(title: String, description: String) = Content.Cell(
    title = title,
    value = this,
    description = description,
)
