package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock
import org.n27.stonks.domain.models.Stocks.Stock.Analysis
import org.n27.stonks.domain.models.Stocks.Stock.BalanceSheet
import org.n27.stonks.domain.models.Stocks.Stock.Computed
import org.n27.stonks.domain.models.Stocks.Stock.Dividends
import org.n27.stonks.domain.models.Stocks.Stock.IncomeStatement
import org.n27.stonks.domain.models.Stocks.Stock.ValuationMeasures

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
    roe: Double? = 152.02099,
    profitMargin: Double? = 27.037,
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
    ),
    revenueEstimate: Analysis.RevenueEstimate? = Analysis.RevenueEstimate(
        growthLow = 2.83,
        growthHigh = 7.22,
    ),
) = Analysis(
    earningsEstimate = earningsEstimate,
    revenueEstimate = revenueEstimate,
)

fun getValuationMeasures(
    pe: Double? = 34.7215522245231,
    valuationFloor: Double? = 12.5,
    intrinsicValue: Double? = 93.375,
) = ValuationMeasures(
    pe = pe,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

fun getComputed(
    earningsYield: Double? = 2.880055573361496,
    peg: Double? = 1.2489766987238526,
    dynamicPayback: Double? = 19.8704990310466,
    cashToEarnings: Double? = 0.6100401606425704,
    cashToPrice: Double? = 1.7569495646329738,
) = Computed(
    earningsYield = earningsYield,
    peg = peg,
    dynamicPayback = dynamicPayback,
    cashToEarnings = cashToEarnings,
    cashToPrice = cashToPrice,
)

fun getBalanceSheet(
    totalCashPerShare: Double? = 4.557,
    de: Double? = 102.63,
) = BalanceSheet(
    totalCashPerShare = totalCashPerShare,
    de = de,
)
