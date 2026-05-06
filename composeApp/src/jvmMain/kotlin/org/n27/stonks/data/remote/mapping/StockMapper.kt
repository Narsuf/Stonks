package org.n27.stonks.data.remote.mapping

import org.n27.stonks.data.remote.model.DividendsRaw
import org.n27.stonks.data.remote.model.EarningsEstimateRaw
import org.n27.stonks.data.remote.model.IncomeStatementRaw
import org.n27.stonks.data.remote.model.StockRaw
import org.n27.stonks.data.remote.model.StocksRaw
import org.n27.stonks.domain.mapping.mapToStock
import org.n27.stonks.domain.model.Stocks
import org.n27.stonks.domain.model.Stocks.Stock.Dividends
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
    dividends = dividends?.toDomain(),
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

private fun DividendsRaw.toDomain() = Dividends(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

private fun IncomeStatementRaw.toDomain() = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

private fun EarningsEstimateRaw.toDomain() = EarningsEstimate(
    growthHigh = growthHigh,
)
