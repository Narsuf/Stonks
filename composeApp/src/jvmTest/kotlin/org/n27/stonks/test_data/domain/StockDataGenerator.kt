package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.model.MacroIndicators
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator
import org.n27.stonks.domain.model.MetricValue
import org.n27.stonks.domain.model.Rating
import org.n27.stonks.domain.model.Stocks
import org.n27.stonks.domain.model.Stocks.Stock
import org.n27.stonks.domain.model.Stocks.Stock.*

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
    earningsEstimate: MetricValue? = getEarningsEstimate(),
    valuationMeasures: ValuationMeasures? = getValuationMeasures(),
    balanceSheet: BalanceSheet? = getBalanceSheet(),
    roe: MetricValue? = getMetricValue(value = 1.5202099, rating = Rating.CAUTION),
    profitMargin: MetricValue? = getMetricValue(value = 0.27037, rating = Rating.CAUTION),
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

fun getMetricValue(
    value: Double = 0.0,
    rating: Rating? = null,
    variation: Double? = null,
) = MetricValue(
    value = value,
    rating = rating,
    variation = variation,
)

fun getDividends(
    dividendYield: MetricValue? = getMetricValue(value = 1.6989447827259432, rating = null),
    payoutRatio: MetricValue? = getMetricValue(value = 58.98999999999989, rating = null),
) = Dividends(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getIncomeStatement(
    eps: MetricValue? = getMetricValue(value = 7.47, rating = null),
    earningsQuarterlyGrowth: MetricValue? = getMetricValue(value = 86.4, rating = null),
) = IncomeStatement(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getEarningsEstimate(
    value: Double = 11.43,
    rating: Rating? = Rating.POSITIVE,
) = getMetricValue(value = value, rating = rating)

fun getValuationMeasures(
    pe: MetricValue? = getMetricValue(value = 34.7215522245231, rating = Rating.WARNING),
    valuationFloor: Double? = 12.5,
    intrinsicValue: Double? = 93.375,
) = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getComputed(
    earningsYield: Double? = 2.880055573361496,
    peg: MetricValue? = getMetricValue(value = 3.037756100133255, rating = Rating.CAUTION),
    dynamicPayback: MetricValue? = getMetricValue(value = 14.812955172783827, rating = null),
) = Computed(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
)

fun getMacroIndicator(
    value: Double = 1.5,
    date: String = "2026-01-01",
) = MacroIndicator(value, date)

fun getMacroIndicators(
    bundYield10Y: MacroIndicator = getMacroIndicator(),
    germanCpi: MacroIndicator = getMacroIndicator(value = 1.9, date = "2025-12"),
    usRealYield10Y: MacroIndicator = getMacroIndicator(value = 1.8),
) = MacroIndicators(
    bundYield10Y = bundYield10Y,
    germanCpi = germanCpi,
    usRealYield10Y = usRealYield10Y,
)

fun getBalanceSheet(
    de: MetricValue? = getMetricValue(value = 102.63, rating = Rating.DANGER),
    totalCashPerShare: MetricValue? = getMetricValue(value = 5.42, rating = null),
) = BalanceSheet(
    de = de,
    totalCashPerShare = totalCashPerShare,
)
