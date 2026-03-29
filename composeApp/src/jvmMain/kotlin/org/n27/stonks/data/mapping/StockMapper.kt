package org.n27.stonks.data.mapping

import org.n27.stonks.data.models.*
import org.n27.stonks.domain.mapping.mapToStock
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock.*
import org.n27.stonks.domain.models.Stocks.Stock.Analysis.EarningsEstimate
import org.n27.stonks.domain.models.Stocks.Stock.Analysis.RevenueEstimate

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
    valuationMeasures = valuationMeasures?.toDomain(),
    balanceSheet = balanceSheet?.toDomain(),
    roe = roe,
    profitMargin = profitMargin,
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

private fun ValuationMeasuresRaw.toDomain() = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

private fun BalanceSheetRaw.toDomain() = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
    currentRatio = currentRatio,
)
