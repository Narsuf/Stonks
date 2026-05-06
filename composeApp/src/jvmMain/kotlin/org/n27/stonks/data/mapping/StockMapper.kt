package org.n27.stonks.data.mapping

import org.n27.stonks.data.model.EarningsEstimateRaw
import org.n27.stonks.data.model.IncomeStatementRaw
import org.n27.stonks.data.model.StockRaw
import org.n27.stonks.data.model.StocksRaw
import org.n27.stonks.domain.mapping.mapToStock
import org.n27.stonks.domain.model.Stocks
import org.n27.stonks.domain.model.Stocks.Stock.EarningsEstimate
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
    dividendYield = dividends?.dividendYield,
    payoutRatio = dividends?.payoutRatio,
    incomeStatement = incomeStatement?.toDomain(),
    earningsEstimate = earningsEstimate?.toDomain(),
    pe = valuationMeasures?.pe,
    valuationFloor = valuationMeasures?.valuationFloor,
    intrinsicValue = valuationMeasures?.intrinsicValue,
    totalCashPerShare = balanceSheet?.totalCashPerShare,
    de = balanceSheet?.de,
    currentRatio = balanceSheet?.currentRatio,
    roe = roe,
    profitMargin = profitMargin,
)

private fun IncomeStatementRaw.toDomain() = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

private fun EarningsEstimateRaw.toDomain() = EarningsEstimate(
    growthHigh = growthHigh,
)
