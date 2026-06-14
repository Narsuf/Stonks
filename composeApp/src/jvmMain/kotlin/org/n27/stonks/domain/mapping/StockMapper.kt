package org.n27.stonks.domain.mapping

import org.n27.stonks.domain.model.RatedValue
import org.n27.stonks.domain.model.Rating
import org.n27.stonks.domain.model.Stocks.Stock
import org.n27.stonks.domain.model.Stocks.Stock.*
import kotlin.math.ln

internal fun mapToStock(
    symbol: String,
    companyName: String,
    logo: String?,
    price: Double?,
    currency: String?,
    lastUpdated: Long?,
    isWatchlisted: Boolean,
    payoutRatio: Double?,
    incomeStatement: IncomeStatement?,
    growthHigh: Double?,
    pe: Double?,
    valuationFloor: Double?,
    intrinsicValue: Double?,
    de: Double?,
    currentRatio: Double?,
    roe: Double?,
    profitMargin: Double?,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = Dividends(
        dividendYield = computeDividendYield(price, incomeStatement?.eps, payoutRatio),
        payoutRatio = payoutRatio?.let { it * 100 }.toRatedValue { toPayoutRatio() },
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
    incomeStatement = incomeStatement,
    earningsEstimate = growthHigh?.toRatedValue { toForwardEarningsGrowth() },
    valuationMeasures = ValuationMeasures(
        pe = pe.toRatedValue { toPeRating() },
        valuationFloor = valuationFloor,
        intrinsicValue = intrinsicValue,
    ),
    balanceSheet = BalanceSheet(
        de = de.toRatedValue { toDeRating() },
        currentRatio = currentRatio.toRatedValue { toCurrentRatioRating() },
    ),
    roe = roe.toRatedValue { toRoeRating() },
    profitMargin = profitMargin.toRatedValue { toProfitMarginRating() },
    computed = Computed(
        earningsYield = computeEarningsYield(pe),
        peg = computePeg(pe, growthHigh),
        dynamicPayback = computeDynamicPayback(price, incomeStatement?.eps, growthHigh),
    ),
)

private fun Double?.toRatedValue(rating: Double.() -> Rating?) = this?.let { RatedValue(it, it.rating()) }

private fun computeDividendYield(price: Double?, eps: Double?, payoutRatio: Double?): Double? {
    if (price == null || eps == null || payoutRatio == null || price <= 0) return null
    val dividend = eps * payoutRatio
    return (dividend / price) * 100
}

private fun Double.toPeRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this > 20 && this <= 25 -> Rating.CAUTION
    this > 25 -> Rating.WARNING
    else -> null
}

private fun Double.toDeRating(): Rating? = when {
    this < 0.3 -> Rating.POSITIVE
    this > 0.5 && this <= 1 -> Rating.CAUTION
    this > 1 -> Rating.DANGER
    else -> null
}

private fun Double.toCurrentRatioRating(): Rating? = when {
    this < 1 -> Rating.CAUTION
    this > 1.5 -> Rating.POSITIVE
    else -> null
}

private fun Double.toRoeRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this > 0 && this < 15 -> Rating.WARNING
    this > 20 -> Rating.POSITIVE
    else -> null
}

private fun Double.toProfitMarginRating(): Rating? = when {
    this < 0 -> Rating.DANGER
    this > 0 && this < 10 -> Rating.WARNING
    this > 20 -> Rating.POSITIVE
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
    this > 1.2 -> Rating.CAUTION
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
    this < 10 -> Rating.POSITIVE
    this > 15 && this <= 20 -> Rating.CAUTION
    this > 20 -> Rating.DANGER
    else -> null
}

private fun Double.toForwardEarningsGrowth(): Rating? = when {
    this < 0 -> Rating.DANGER
    this in 10.0..15.0 -> Rating.POSITIVE
    this !in 5.0..15.0 -> Rating.CAUTION
    else -> null
}

private fun Double.toPayoutRatio(): Rating? = when {
    this in 75.0..90.0 -> Rating.CAUTION
    this > 90 -> Rating.DANGER
    else -> null
}
