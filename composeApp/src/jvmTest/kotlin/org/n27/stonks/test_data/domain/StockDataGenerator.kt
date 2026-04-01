package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.models.MacroIndicators
import org.n27.stonks.domain.models.RatedValue
import org.n27.stonks.domain.models.Rating
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.domain.models.Stocks.Stock.*

fun getStocks(
    items: List<Stock> = listOf(getStock()),
    nextPage: Int? = 2,
) = Stocks(
    items = items,
    nextPage = nextPage,
)

fun getStock(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String? = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB...",
    price: Double? = 259.369995117188,
    dividends: Dividends? = getDividends(),
    currency: String? = "USD",
    lastUpdated: Long? = 1768064114877,
    isWatchlisted: Boolean = false,
    incomeStatement: IncomeStatement? = getIncomeStatement(),
    earningsEstimate: EarningsEstimate? = getEarningsEstimate(),
    valuationMeasures: ValuationMeasures? = getValuationMeasures(),
    balanceSheet: BalanceSheet? = getBalanceSheet(),
    roe: RatedValue? = getRatedValue(value = 1.5202099, rating = Rating.CAUTION),
    profitMargin: RatedValue? = getRatedValue(value = 0.27037, rating = Rating.CAUTION),
    computed: Computed? = getComputed(),
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = dividends,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
    incomeStatement = incomeStatement,
    earningsEstimate = earningsEstimate,
    valuationMeasures = valuationMeasures,
    balanceSheet = balanceSheet,
    roe = roe,
    profitMargin = profitMargin,
    computed = computed,
)

fun getRatedValue(
    value: Double = 0.0,
    rating: Rating? = null,
) = RatedValue(
    value = value,
    rating = rating,
)

fun getDividends(
    dividendYield: Double? = 0.4,
    payoutRatio: Double? = 0.5899,
) = Dividends(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getIncomeStatement(
    eps: Double? = 7.47,
    earningsQuarterlyGrowth: Double? = 86.4,
) = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getEarningsEstimate(
    growthHigh: Double? = 11.43,
) = EarningsEstimate(
    growthHigh = growthHigh,
)

fun getValuationMeasures(
    pe: RatedValue? = getRatedValue(value = 34.7215522245231, rating = Rating.DANGER),
    valuationFloor: Double? = 12.5,
    intrinsicValue: Double? = 93.375,
) = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getComputed(
    earningsYield: Double? = 2.880055573361496,
    peg: RatedValue? = getRatedValue(value = 3.037756100133255, rating = Rating.DANGER),
    dynamicPayback: RatedValue? = getRatedValue(value = 14.812955172783827, rating = null),
    cashToEarnings: RatedValue? = getRatedValue(value = 0.6100401606425704, rating = Rating.CAUTION),
) = Computed(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
    cashToEarnings = cashToEarnings,
)

fun getMacroIndicators(
    treasury10Y: Double = 1.5,
    treasury10YDate: String? = "2026-03-30",
    europeanTreasury10Y: Double = 2.5,
    europeanTreasury10YDate: String? = "2026-01-01",
    corporateAAA: Double = 3.0,
    germanCpi: Double? = 1.9,
    germanCpiDate: String? = "2025-12",
) = MacroIndicators(
    treasury10Y = treasury10Y,
    treasury10YDate = treasury10YDate,
    europeanTreasury10Y = europeanTreasury10Y,
    europeanTreasury10YDate = europeanTreasury10YDate,
    corporateAAA = corporateAAA,
    germanCpi = germanCpi,
    germanCpiDate = germanCpiDate,
)

fun getBalanceSheet(
    totalCashPerShare: Double? = 4.557,
    de: RatedValue? = getRatedValue(value = 102.63, rating = Rating.DANGER),
    currentRatio: RatedValue? = getRatedValue(value = 0.955, rating = null),
) = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
    currentRatio = currentRatio,
)
