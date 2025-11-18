package org.n27.stonks.data.mapping

import org.n27.stonks.data.model.StockRaw
import org.n27.stonks.domain.domain.Stock
import org.n27.stonks.domain.domain.Stocks

internal fun List<StockRaw>.toDomainEntity() = Stocks(
    items = map { it.toDomainEntity() }
)

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