package org.n27.stonks.test_data.data

import org.n27.stonks.data.models.AnalysisRaw
import org.n27.stonks.data.models.BalanceSheetRaw
import org.n27.stonks.data.models.EarningsEstimateRaw
import org.n27.stonks.data.models.IncomeStatementRaw
import org.n27.stonks.data.models.RevenueEstimateRaw
import org.n27.stonks.data.models.StockRaw
import org.n27.stonks.data.models.StocksRaw
import org.n27.stonks.data.models.ValuationMeasuresRaw

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
    dividendYield: Double? = 0.4,
    incomeStatement: IncomeStatementRaw? = getIncomeStatementRaw(),
    analysis: AnalysisRaw? = getAnalysisRaw(),
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
    dividendYield = dividendYield,
    incomeStatement = incomeStatement,
    analysis = analysis,
    valuationMeasures = valuationMeasures,
    balanceSheet = balanceSheet,
    roe = roe,
    profitMargin = profitMargin,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

fun getIncomeStatementRaw(
    eps: Double? = 7.47,
    earningsQuarterlyGrowth: Double? = 86.4,
    revenueQuarterlyGrowth: Double? = 9.7,
) = IncomeStatementRaw(
    eps = eps,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    revenueQuarterlyGrowth = revenueQuarterlyGrowth,
)

fun getAnalysisRaw(
    earningsEstimate: EarningsEstimateRaw? = EarningsEstimateRaw(
        growthLow = 5.56,
        growthHigh = 11.43,
    ),
    revenueEstimate: RevenueEstimateRaw? = RevenueEstimateRaw(
        growthLow = 2.83,
        growthHigh = 7.22,
    ),
) = AnalysisRaw(
    earningsEstimate = earningsEstimate,
    revenueEstimate = revenueEstimate,
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
) = BalanceSheetRaw(
    totalCashPerShare = totalCashPerShare,
    de = de,
)
