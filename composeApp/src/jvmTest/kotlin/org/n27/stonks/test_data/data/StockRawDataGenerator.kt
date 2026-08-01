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
    earningsEstimate: RatedValueRaw? = getRatedValueRaw(value = 11.43, rating = RatingRaw.POSITIVE),
    valuationMeasures: ValuationMeasuresRaw? = getValuationMeasuresRaw(),
    balanceSheet: BalanceSheetRaw? = getBalanceSheetRaw(),
    roe: RatedValueRaw? = getRatedValueRaw(value = 1.5202099, rating = RatingRaw.CAUTION),
    profitMargin: RatedValueRaw? = getRatedValueRaw(value = 0.27037, rating = RatingRaw.CAUTION),
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

fun getRatedValueRaw(
    value: Double = 0.0,
    rating: RatingRaw? = null,
) = RatedValueRaw(
    value = value,
    rating = rating,
)

fun getDividendsRaw(
    dividendYield: Double? = 1.6989447827259432,
    payoutRatio: RatedValueRaw? = getRatedValueRaw(value = 58.98999999999989, rating = null),
) = DividendsRaw(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getComputedRaw(
    earningsYield: Double? = 2.880055573361496,
    peg: RatedValueRaw? = getRatedValueRaw(value = 3.037756100133255, rating = RatingRaw.CAUTION),
    dynamicPayback: RatedValueRaw? = getRatedValueRaw(value = 14.812955172783827, rating = null),
) = ComputedRaw(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
)

fun getIncomeStatementRaw(
    eps: Double? = 7.47,
    earningsQuarterlyGrowth: Double? = 86.4,
) = IncomeStatementRaw(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getValuationMeasuresRaw(
    pe: RatedValueRaw? = getRatedValueRaw(value = 34.7215522245231, rating = RatingRaw.WARNING),
    valuationFloor: Double? = 12.5,
    intrinsicValue: Double? = 93.375,
) = ValuationMeasuresRaw(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getBalanceSheetRaw(
    de: RatedValueRaw? = getRatedValueRaw(value = 102.63, rating = RatingRaw.DANGER),
    totalCashPerShare: Double? = 5.42,
) = BalanceSheetRaw(
    de = de,
    totalCashPerShare = totalCashPerShare,
)
