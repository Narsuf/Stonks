package org.n27.stonks.presentation.detail.mapping

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.models.common.Stock
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.detail.entities.DetailState.Content

internal fun Stock.toDetailContent() = Content(
    symbol = symbol,
    logoUrl = logoUrl ?: "",
    name = companyName.truncateAfterDoubleSpace(),
    price = price?.toFormattedBigDecimal()?.toPrice(currency),
    targetPrice = price?.getTargetPrice(currentIntrinsicValue, currency),
    cells = buildList {
        dividendYield?.toDividendCell()?.let(::add)
        eps?.toEpsCell(currency)?.let(::add)
        pe?.toPe()?.let(::add)
        earningsQuarterlyGrowth?.toGrowth()?.let(::add)
        currentIntrinsicValue?.toIntrinsicValue(currency)?.let(::add)
        forwardIntrinsicValue?.let { it.toForwardIntrinsicValue(currency)?.let(::add) }
    }.toPersistentList(),
)

private fun Double.toDividendCell() = toFormattedPercentage().toCell(
    title = "Dividend yield",
    description = "How much a company pays in dividends each year relative to its stock price.",
)

private fun Double.toEpsCell(currency: String?) = toFormattedBigDecimal().toPrice(currency)?.toCell(
    title = "EPS",
    description = "The company’s earnings divided by the total number of shares.",
)

private fun Double.toPe() = toFormattedString().toCell(
    title = "P/E",
    description = "The stock price divided by its earnings per share.\n\n" +
            "A P/E around 12.5 is traditionally considered a reasonable value.",
)

private fun Double.toGrowth() = toFormattedPercentage().toCell(
    title = "Growth",
    description = "Quarterly earnings growth (YoY).\n\n" +
            "The company’s growth compared to the same quarter of the previous year.",
)

private fun Double.toIntrinsicValue(currency: String?) = toFormattedBigDecimal().toPrice(currency)?.toCell(
    title = "Intrinsic Value",
    description = "The estimated intrinsic value based on an ideal P/E ratio of 12.5."
)

private fun Double.toForwardIntrinsicValue(currency: String?) = toFormattedBigDecimal().toPrice(currency)?.toCell(
    title = "Forward Intrinsic Value",
    description = "The estimated intrinsic value including the expected EPS growth for the next year."
)

private fun String.toCell(title: String, description: String) = Content.Cell(
    title = title,
    value = this,
    description = description,
)
