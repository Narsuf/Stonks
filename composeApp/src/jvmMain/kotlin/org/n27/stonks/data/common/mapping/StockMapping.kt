package org.n27.stonks.data.common.mapping

import org.n27.stonks.data.common.model.StockRaw
import org.n27.stonks.domain.common.Stock

private fun StockRaw.toDomainEntity() = Stock(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price,
    eps = eps,
    trailingPe = trailingPe,
    dividendYield = dividendYield,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    currency = currency,
    intrinsicValue = intrinsicValue,
)
