package org.n27.stonks.domain.mapping

import org.n27.stonks.domain.models.RatedValue
import org.n27.stonks.domain.models.Rating
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
    earningsEstimate: EarningsEstimate?,
    pe: Double?,
    valuationFloor: Double?,
    intrinsicValue: Double?,
    totalCashPerShare: Double?,
    de: Double?,
    currentRatio: Double?,
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
    earningsEstimate = earningsEstimate,
    valuationMeasures = ValuationMeasures(
        pe = pe.toRatedValue { toPeRating() },
        valuationFloor = valuationFloor,
        intrinsicValue = intrinsicValue,
    ),
    balanceSheet = BalanceSheet(
        totalCashPerShare = totalCashPerShare,
        de = de.toRatedValue { toDeRating() },
        currentRatio = currentRatio.toRatedValue { toCurrentRatioRating() },
    ),
    roe = roe.toRatedValue { toRoeRating() },
    profitMargin = profitMargin.toRatedValue { toProfitMarginRating() },
    computed = Computed(
        earningsYield = computeEarningsYield(pe),
        peg = computePeg(pe, earningsEstimate?.growthAvg),
        dynamicPayback = computeDynamicPayback(price, incomeStatement?.eps, earningsEstimate?.growthAvg),
        cashToEarnings = computeCashToEarnings(totalCashPerShare, incomeStatement?.eps),
    ),
)

private fun Double?.toRatedValue(rating: Double.() -> Rating?) = this?.let { RatedValue(it, it.rating()) }

private fun Double.toPeRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this > 0 && this < 5 -> Rating.CAUTION
    this > 20 && this <= 25 -> Rating.CAUTION
    this > 25 && this <= 30 -> Rating.WARNING
    this > 30 -> Rating.DANGER
    else -> null
}

private fun Double.toDeRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this < 1 -> Rating.POSITIVE
    this > 2 && this <= 3 -> Rating.CAUTION
    this > 3 -> Rating.DANGER
    else -> null
}

private fun Double.toCurrentRatioRating(): Rating? = when {
    this < 0.5 -> Rating.CAUTION
    this > 1 -> Rating.POSITIVE
    else -> null
}

private fun Double.toRoeRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this > 0 && this < 8 -> Rating.CAUTION
    this > 15 -> Rating.POSITIVE
    else -> null
}

private fun Double.toProfitMarginRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this > 0 && this < 5 -> Rating.CAUTION
    this > 15 -> Rating.POSITIVE
    else -> null
}

private fun computeEarningsYield(pe: Double?) = pe
    ?.takeIf { it != 0.0 }
    ?.let { (1.0 / it) * 100 }

private fun computePeg(pe: Double?, growth: Double?) = pe?.let { p ->
    growth
        ?.takeIf { it > 0 }
        ?.let { (p / it).toRatedValue { toPegRating() } }
}

private fun Double.toPegRating(): Rating? = when {
    this > 1.5 && this <= 2 -> Rating.CAUTION
    this > 2 && this <= 3 -> Rating.WARNING
    this > 3 -> Rating.DANGER
    else -> null
}

private fun computeDynamicPayback(price: Double?, eps: Double?, growth: Double?): RatedValue? {
    if (price == null || eps == null || growth == null || eps <= 0 || growth <= 0) return null
    val g = growth / 100
    val numerator = ln(1 + price * g / eps)
    val denominator = ln(1 + g)
    return (numerator / denominator)
        .takeIf { numerator > 0 }
        .toRatedValue { toDynamicPaybackRating() }
}

private fun Double.toDynamicPaybackRating(): Rating? = when {
    this > 15 && this <= 20 -> Rating.CAUTION
    this > 20 && this <= 25 -> Rating.WARNING
    this > 25 -> Rating.DANGER
    else -> null
}

private fun computeCashToEarnings(cash: Double?, eps: Double?) = cash?.let { c ->
    eps
        ?.takeIf { it != 0.0 }
        ?.let { (c / it).toRatedValue { toCashToEarningsRating() } }
}

private fun Double.toCashToEarningsRating(): Rating? = when {
    this < 1 -> Rating.CAUTION
    else -> null
}

