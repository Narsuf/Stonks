package org.n27.stonks.data.yfinance.mapping

import org.n27.stonks.data.yfinance.model.StockRaw
import org.n27.stonks.domain.common.Stock
import org.n27.stonks.domain.common.Stocks

internal fun List<StockRaw>.toDomainEntity(nextPage: Int? = null) = Stocks(
    nextPage = nextPage,
    items = map { it.toDomainEntity() }
)

internal fun StockRaw.toDomainEntity() = Stock(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    expectedEpsGrowth = null,
    currentIntrinsicValue = intrinsicValue,
    forwardIntrinsicValue = null,
    currency = currency,
    lastUpdated = null,
)
