package org.n27.stonks.data.mapping

import org.n27.stonks.data.models.*
import org.n27.stonks.domain.mapping.mapToStock
import org.n27.stonks.domain.models.RatedValue
import org.n27.stonks.domain.models.Rating
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

private fun ValuationMeasuresRaw.toDomain() = ValuationMeasures(
    pe = pe?.let { RatedValue(value = it, rating = it.toPeRating()) },
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

private fun Double.toPeRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this > 0 && this < 5 -> Rating.CAUTION
    this > 20 && this <= 25 -> Rating.CAUTION
    this > 25 && this <= 30 -> Rating.WARNING
    this > 30 -> Rating.DANGER
    else -> null
}

private fun BalanceSheetRaw.toDomain() = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de?.let {
        val result = it / 100
        RatedValue(
            value = result,
            rating = result.toDeRating(),
        )
    },
    currentRatio = currentRatio?.let {
        RatedValue(
            value = it,
            rating = it.toCurrentRatioRating(),
        )
    },
)

private fun Double.toDeRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this < 1 -> Rating.POSITIVE
    this > 2 && this <= 3 -> Rating.CAUTION
    this > 3  -> Rating.DANGER
    else -> null
}

private fun Double.toCurrentRatioRating(): Rating? = when {
    this < 0.5 -> Rating.CAUTION
    this > 1 -> Rating.POSITIVE
    else -> null
}
