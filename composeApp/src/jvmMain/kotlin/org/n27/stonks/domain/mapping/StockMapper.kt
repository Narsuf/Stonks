package org.n27.stonks.domain.mapping

import kotlin.math.ln
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.domain.models.Stocks.Stock.Analysis
import org.n27.stonks.domain.models.Stocks.Stock.BalanceSheet
import org.n27.stonks.domain.models.Stocks.Stock.Computed
import org.n27.stonks.domain.models.Stocks.Stock.Dividends
import org.n27.stonks.domain.models.Stocks.Stock.IncomeStatement
import org.n27.stonks.domain.models.Stocks.Stock.ValuationMeasures

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
        earningsYield = computeEarningsYield(valuationMeasures?.pe),
        peg = computePeg(valuationMeasures?.pe, analysis?.earningsEstimate?.growthLow),
        dynamicPayback = computeDynamicPayback(price, incomeStatement?.eps, analysis?.earningsEstimate?.growthLow),
        cashToEarnings = computeCashToEarnings(balanceSheet?.totalCashPerShare, incomeStatement?.eps),
        cashToPrice = computeCashToPrice(price, balanceSheet?.totalCashPerShare),
    ),
)

private fun computeEarningsYield(pe: Double?) = pe
    ?.takeIf { it != 0.0 }
    ?.let { (1.0 / it) * 100 }

private fun computePeg(pe: Double?, growthLow: Double?) = pe?.let { p ->
    growthLow
        ?.takeIf { it > 0 }
        ?.let { p / (it * 5) }
}

private fun computeDynamicPayback(price: Double?, eps: Double?, growthLow: Double?): Double? {
    if (price == null || eps == null || growthLow == null || eps <= 0 || growthLow <= 0) return null
    val g = growthLow / 100
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
