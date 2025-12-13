package org.n27.stonks.data.yfinance.mapping

import org.n27.stonks.data.watchlist.StockInfo
import org.n27.stonks.data.yfinance.model.StockRaw
import org.n27.stonks.domain.mapping.recalculateIntrinsicValues
import org.n27.stonks.domain.models.Stock
import org.n27.stonks.domain.models.Stocks

internal fun List<StockRaw>.toDomainEntity(nextPage: Int?) = Stocks(
    nextPage = nextPage,
    items = map { it.toDomainEntity() }
)

internal fun List<StockRaw>.toDomainEntity(watchlist: List<StockInfo>, nextPage: Int?) = Stocks(
    nextPage = nextPage,
    items = mapIndexed { index, item -> item.toDomainEntity(watchlist[index]) },
)

internal fun StockRaw.toDomainEntity(stockInfo: StockInfo?) = stockInfo?.expectedEpsGrowth
    ?.let { toDomainEntity().recalculateIntrinsicValues(it, stockInfo.valuationFloor) }
    ?: toDomainEntity()

private fun StockRaw.toDomainEntity() = Stock(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    expectedEpsGrowth = null,
    valuationFloor = null,
    currentIntrinsicValue = intrinsicValue,
    forwardIntrinsicValue = null,
    currency = currency,
    lastUpdated = null,
)
