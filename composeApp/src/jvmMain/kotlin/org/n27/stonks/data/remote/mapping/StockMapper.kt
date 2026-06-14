package org.n27.stonks.data.remote.mapping

import org.n27.stonks.data.remote.model.EarningsEstimateRaw
import org.n27.stonks.data.remote.model.IncomeStatementRaw
import org.n27.stonks.data.remote.model.StockRaw
import org.n27.stonks.data.remote.model.StocksRaw
import org.n27.stonks.domain.mapping.mapToStock
import org.n27.stonks.domain.model.Stocks
import org.n27.stonks.domain.model.Stocks.Stock.IncomeStatement

internal fun StocksRaw.toDomain() = Stocks(
    items = items.map { it.toDomain() },
    nextPage = nextPage,
)

internal fun StockRaw.toDomain() = mapToStock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
    payoutRatio = dividends?.payoutRatio,
    incomeStatement = incomeStatement?.toDomain(),
    growthHigh = earningsEstimate?.growthHigh,
    pe = valuationMeasures?.pe,
    valuationFloor = valuationMeasures?.valuationFloor,
    intrinsicValue = valuationMeasures?.intrinsicValue,
    de = balanceSheet?.de,
    currentRatio = balanceSheet?.currentRatio,
    roe = roe,
    profitMargin = profitMargin,
)


private fun IncomeStatementRaw.toDomain() = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)
