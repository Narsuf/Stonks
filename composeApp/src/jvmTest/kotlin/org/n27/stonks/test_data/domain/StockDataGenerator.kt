package org.n27.stonks.test_data.domain

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
    dividendYield: Double? = 0.4,
    incomeStatement: IncomeStatement? = getIncomeStatement(),
    analysis: Analysis? = getAnalysis(),
    valuationMeasures: ValuationMeasures? = getValuationMeasures(),
    currency: String? = "USD",
    lastUpdated: Long? = 1768064114877,
    isWatchlisted: Boolean = false,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividendYield = dividendYield,
    incomeStatement = incomeStatement,
    analysis = analysis,
    valuationMeasures = valuationMeasures,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
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
    pb: Double? = 51.967537,
    ps: Double? = 8.23,
    valuationFloor: Double? = 12.5,
    intrinsicValue: Double? = 93.375,
) = ValuationMeasures(
    pe = pe,
    pb = pb,
    ps = ps,
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)
