package org.n27.stonks.presentation.detail.mapping

import org.n27.stonks.domain.common.Stock
import org.n27.stonks.presentation.common.extensions.toFormattedBigDecimal
import org.n27.stonks.presentation.common.extensions.toFormattedString
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.common.extensions.truncateAfterDoubleSpace
import org.n27.stonks.presentation.detail.entities.DetailState.Content

internal fun Stock.toDetailContent() = Content(
    symbol = symbol,
    logoUrl = logoUrl ?: "",
    name = companyName.truncateAfterDoubleSpace(),
    price = price?.toFormattedBigDecimal()?.toPrice(currency),
    eps = eps?.toFormattedString(),
    trailingPe = trailingPe?.toFormattedString(),
    dividendYield = dividendYield?.toFormattedString(),
    earningsQuarterlyGrowth = earningsQuarterlyGrowth?.toFormattedString(),
    intrinsicValue = intrinsicValue?.toFormattedString(),
)
