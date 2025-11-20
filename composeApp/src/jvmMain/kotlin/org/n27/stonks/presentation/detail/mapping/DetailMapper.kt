package org.n27.stonks.presentation.detail.mapping

import org.n27.stonks.domain.common.Stock
import org.n27.stonks.presentation.common.extensions.toFormattedString
import org.n27.stonks.presentation.detail.entities.DetailState.Content


internal fun Stock.toDetailContent() = Content(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price?.toFormattedString(),
    eps = eps?.toFormattedString(),
    trailingPe = trailingPe?.toFormattedString(),
    dividendYield = dividendYield?.toFormattedString(),
    earningsQuarterlyGrowth = earningsQuarterlyGrowth?.toFormattedString(),
    intrinsicValue = intrinsicValue?.toFormattedString(),
)
