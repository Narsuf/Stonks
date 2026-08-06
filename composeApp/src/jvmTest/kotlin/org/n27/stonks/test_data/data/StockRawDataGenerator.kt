package org.n27.stonks.test_data.data

import org.n27.stonks.data.remote.model.*

fun getStocksRaw(
    items: List<StockRaw> = listOf(getStockRaw()),
    nextPage: Int? = 2,
) = StocksRaw(
    items = items,
    nextPage = nextPage,
)

fun getStockRaw(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String? = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB...",
    price: Double? = 259.369995117188,
    dividends: DividendsRaw? = getDividendsRaw(),
    incomeStatement: IncomeStatementRaw? = getIncomeStatementRaw(),
    earningsEstimate: MetricValueRaw? = getMetricValueRaw(value = 11.43, rating = RatingRaw.POSITIVE),
    valuationMeasures: ValuationMeasuresRaw? = getValuationMeasuresRaw(),
    balanceSheet: BalanceSheetRaw? = getBalanceSheetRaw(),
    roe: MetricValueRaw? = getMetricValueRaw(value = 1.5202099, rating = RatingRaw.CAUTION),
    profitMargin: MetricValueRaw? = getMetricValueRaw(value = 0.27037, rating = RatingRaw.CAUTION),
    computed: ComputedRaw? = getComputedRaw(),
    currency: String? = "USD",
    lastUpdated: Long? = 1768064114877,
    isWatchlisted: Boolean = false,
) = StockRaw(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = dividends,
    incomeStatement = incomeStatement,
    earningsEstimate = earningsEstimate,
    valuationMeasures = valuationMeasures,
    balanceSheet = balanceSheet,
    roe = roe,
    profitMargin = profitMargin,
    computed = computed,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun getMetricValueRaw(
    value: Double = 0.0,
    rating: RatingRaw? = null,
    variation: Double? = null,
) = MetricValueRaw(
    value = value,
    rating = rating,
    variation = variation,
)

fun getDividendsRaw(
    dividendYield: MetricValueRaw? = getMetricValueRaw(value = 1.6989447827259432, rating = null),
    payoutRatio: MetricValueRaw? = getMetricValueRaw(value = 58.98999999999989, rating = null),
) = DividendsRaw(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getComputedRaw(
    earningsYield: Double? = 2.880055573361496,
    peg: MetricValueRaw? = getMetricValueRaw(value = 3.037756100133255, rating = RatingRaw.CAUTION),
    dynamicPayback: MetricValueRaw? = getMetricValueRaw(value = 14.812955172783827, rating = null),
) = ComputedRaw(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
)

fun getIncomeStatementRaw(
    eps: MetricValueRaw? = getMetricValueRaw(value = 7.47, rating = null),
    earningsQuarterlyGrowth: MetricValueRaw? = getMetricValueRaw(value = 86.4, rating = null),
) = IncomeStatementRaw(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getValuationMeasuresRaw(
    pe: MetricValueRaw? = getMetricValueRaw(value = 34.7215522245231, rating = RatingRaw.WARNING),
    valuationFloor: Double? = 12.5,
    intrinsicValue: Double? = 93.375,
) = ValuationMeasuresRaw(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getBalanceSheetRaw(
    de: MetricValueRaw? = getMetricValueRaw(value = 102.63, rating = RatingRaw.DANGER),
    totalCashPerShare: MetricValueRaw? = getMetricValueRaw(value = 5.42, rating = null),
) = BalanceSheetRaw(
    de = de,
    totalCashPerShare = totalCashPerShare,
)
