package org.n27.stonks.test_data.domain

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
    analysis: Analysis? = getAnalysis(),
    valuationMeasures: ValuationMeasures? = getValuationMeasures(),
    balanceSheet: BalanceSheet? = getBalanceSheet(),
    roe: RatedValue? = getRatedValue(value = 152.02099, rating = Rating.POSITIVE),
    profitMargin: RatedValue? = getRatedValue(value = 27.037, rating = Rating.POSITIVE),
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
    analysis = analysis,
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
    revenueQuarterlyGrowth: Double? = 9.7,
) = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    revenueQuarterlyGrowth = revenueQuarterlyGrowth,
)

fun getAnalysis(
    earningsEstimate: Analysis.EarningsEstimate? = Analysis.EarningsEstimate(
        growthLow = 5.56,
        growthHigh = 11.43,
        growthAvg = 8.65,
    ),
    revenueEstimate: Analysis.RevenueEstimate? = Analysis.RevenueEstimate(
        growthLow = 2.83,
        growthHigh = 7.22,
        growthAvg = 5.54,
    ),
) = Analysis(
    earningsEstimate = earningsEstimate,
    revenueEstimate = revenueEstimate,
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
    peg: RatedValue? = getRatedValue(value = 4.014052280291688, rating = Rating.DANGER),
    dynamicPayback: RatedValue? = getRatedValue(value = 16.720372914455112, rating = Rating.CAUTION),
    cashToEarnings: RatedValue? = getRatedValue(value = 0.6100401606425704, rating = Rating.CAUTION),
) = Computed(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
    cashToEarnings = cashToEarnings,
)

fun getBalanceSheet(
    totalCashPerShare: Double? = 4.557,
    de: RatedValue? = getRatedValue(value = 1.0263, rating = null),
    currentRatio: RatedValue? = getRatedValue(value = 0.955, rating = null),
) = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
    currentRatio = currentRatio,
)
