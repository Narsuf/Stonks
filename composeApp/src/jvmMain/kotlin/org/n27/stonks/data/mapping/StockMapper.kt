package org.n27.stonks.data.mapping

import org.n27.stonks.data.models.*
import org.n27.stonks.domain.mapping.mapToStock
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock.Analysis
import org.n27.stonks.domain.models.Stocks.Stock.Analysis.EarningsEstimate
import org.n27.stonks.domain.models.Stocks.Stock.Analysis.RevenueEstimate
import org.n27.stonks.domain.models.Stocks.Stock.IncomeStatement

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
    analysis = analysis?.toDomain(),
    pe = valuationMeasures?.pe,
    valuationFloor = valuationMeasures?.valuationFloor,
    intrinsicValue = valuationMeasures?.intrinsicValue,
    totalCashPerShare = balanceSheet?.totalCashPerShare,
    de = balanceSheet?.de?.let { it / 100 },
    currentRatio = balanceSheet?.currentRatio,
    roe = roe?.let { it * 100 },
    profitMargin = profitMargin?.let { it * 100 },
)

private fun IncomeStatementRaw.toDomain() = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    revenueQuarterlyGrowth = revenueQuarterlyGrowth,
)

private fun AnalysisRaw.toDomain() = Analysis(
    earningsEstimate = earningsEstimate?.let {
        EarningsEstimate(growthLow = it.growthLow, growthHigh = it.growthHigh, growthAvg = it.growthAvg)
    },
    revenueEstimate = revenueEstimate?.toDomain(),
)

private fun RevenueEstimateRaw.toDomain() = RevenueEstimate(
    growthLow = growthLow,
    growthHigh = growthHigh,
    growthAvg = growthAvg,
)

