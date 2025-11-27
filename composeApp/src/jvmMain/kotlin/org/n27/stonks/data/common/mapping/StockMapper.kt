package org.n27.stonks.data.common.mapping

import org.n27.stonks.data.common.model.StockRaw
import org.n27.stonks.domain.models.common.Stock
import org.n27.stonks.domain.models.common.Stocks

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
)
