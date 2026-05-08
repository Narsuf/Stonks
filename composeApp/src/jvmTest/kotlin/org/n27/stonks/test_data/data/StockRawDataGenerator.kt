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
    earningsEstimate: EarningsEstimateRaw? = getEarningsEstimateRaw(),
    valuationMeasures: ValuationMeasuresRaw? = getValuationMeasuresRaw(),
    balanceSheet: BalanceSheetRaw? = getBalanceSheetRaw(),
    roe: Double? = 1.5202099,
    profitMargin: Double? = 0.27037,
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
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun getDividendsRaw(
    dividendYield: Double? = 0.4,
    payoutRatio: Double? = 0.5899,
) = DividendsRaw(
    dividendYield = dividendYield,
    payoutRatio = payoutRatio,
)

fun getIncomeStatementRaw(
    eps: Double? = 7.47,
    earningsQuarterlyGrowth: Double? = 86.4,
) = IncomeStatementRaw(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
)

fun getEarningsEstimateRaw(
    growthHigh: Double? = 11.43,
    growthAvg: Double? = 8.65,
) = EarningsEstimateRaw(
    growthHigh = growthHigh,
    growthAvg = growthAvg,
)

fun getValuationMeasuresRaw(
    pe: Double? = 34.7215522245231,
    valuationFloor: Double? = 12.5,
    intrinsicValue: Double? = 93.375,
) = ValuationMeasuresRaw(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getBalanceSheetRaw(
    totalCashPerShare: Double? = 4.557,
    de: Double? = 102.63,
    currentRatio: Double? = 0.955,
) = BalanceSheetRaw(
    totalCashPerShare = totalCashPerShare,
    de = de,
    currentRatio = currentRatio,
)
