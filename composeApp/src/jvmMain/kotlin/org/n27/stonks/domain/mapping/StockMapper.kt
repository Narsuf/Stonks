package org.n27.stonks.domain.mapping

import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.domain.models.Stocks.Stock.*
import kotlin.math.ln

internal fun mapToStock(
    symbol: String,
    companyName: String,
    logo: String?,
    price: Double?,
    currency: String?,
    lastUpdated: Long?,
    isWatchlisted: Boolean,
    dividendYield: Double?,
    payoutRatio: Double?,
    incomeStatement: IncomeStatement?,
    analysis: Analysis?,
    valuationMeasures: ValuationMeasures?,
    balanceSheet: BalanceSheet?,
    roe: Double?,
    profitMargin: Double?,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = Dividends(dividendYield = dividendYield, payoutRatio = payoutRatio),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
    incomeStatement = incomeStatement,
    analysis = analysis,
    valuationMeasures = valuationMeasures,
    balanceSheet = balanceSheet,
    roe = roe?.let { it * 100 },
    profitMargin = profitMargin?.let { it * 100 },
    computed = Computed(
        earningsYield = computeEarningsYield(valuationMeasures?.pe?.value),
        peg = computePeg(valuationMeasures?.pe?.value, analysis?.earningsEstimate?.growthAvg),
        dynamicPayback = computeDynamicPayback(price, incomeStatement?.eps, analysis?.earningsEstimate?.growthAvg),
        cashToEarnings = computeCashToEarnings(balanceSheet?.totalCashPerShare, incomeStatement?.eps),
        cashToPrice = computeCashToPrice(price, balanceSheet?.totalCashPerShare),
    ),
)

private fun computeEarningsYield(pe: Double?) = pe
    ?.takeIf { it != 0.0 }
    ?.let { (1.0 / it) * 100 }

private fun computePeg(pe: Double?, growth: Double?) = pe?.let { p ->
    growth
        ?.takeIf { it > 0 }
        ?.let { p / (it * 5) }
}

private fun computeDynamicPayback(price: Double?, eps: Double?, growth: Double?): Double? {
    if (price == null || eps == null || growth == null || eps <= 0 || growth <= 0) return null
    val g = growth / 100
    val numerator = ln(1 + price * g / eps)
    val denominator = ln(1 + g)
    return (numerator / denominator).takeIf { denominator > 0 }
}

private fun computeCashToEarnings(cash: Double?, eps: Double?) = cash?.let { c ->
    eps
        ?.takeIf { it != 0.0 }
        ?.let { c / it }
}

private fun computeCashToPrice(price: Double?, cash: Double?) = price?.takeIf { it != 0.0 }
    ?.let { p ->
        cash?.let { (it / p) * 100 }
    }
