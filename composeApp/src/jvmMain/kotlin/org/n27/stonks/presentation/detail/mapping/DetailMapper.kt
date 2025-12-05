package org.n27.stonks.presentation.detail.mapping

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.models.Stock
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.detail.entities.DetailState.Content

internal fun Stock.toDetailContent() = Content(
    symbol = symbol,
    logoUrl = logoUrl ?: "",
    name = companyName.truncateAfterDoubleSpace(),
    price = price?.toPrice(currency),
    cells = buildList {
        dividendYield?.toDividendCell()?.let(::add)
        eps?.toEpsCell(currency)?.let(::add)
        earningsQuarterlyGrowth?.toGrowth()?.let(::add)
        expectedEpsGrowth?.toExpectedEpsGrowth()?.let(::add)
        currentIntrinsicValue?.toIntrinsicValue(this@toDetailContent)?.let(::add)
        forwardIntrinsicValue?.toForwardIntrinsicValue(this@toDetailContent)?.let(::add)
        pe?.toPe()?.let(::add)
    }.toPersistentList(),
)

private fun Double.toDividendCell() = toFormattedPercentage().toCell(
    title = "Dividend yield",
    description = "How much a company pays in dividends each year relative to its stock price.",
)

private fun Double.toEpsCell(currency: String?) = toPrice(currency)?.toCell(
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

private fun Double.toExpectedEpsGrowth() = toFormattedPercentage().toCell(
    title = "Forward Growth",
    description = "Expected EPS growth.\n\n" +
            "The company’s expected EPS growth for the next year."
)

private fun Double.toIntrinsicValue(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = "Intrinsic Value",
    description = "The estimated intrinsic value based on an ideal P/E ratio of 12.5.",
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

private fun Double.toForwardIntrinsicValue(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = "Forward Intrinsic Value",
    description = "The estimated intrinsic value including the expected EPS growth for the next year.",
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

private fun String.toCell(title: String, description: String, delta: DeltaTextEntity? = null) = Content.Cell(
    title = title,
    value = this,
    description = description,
    delta = delta,
)
