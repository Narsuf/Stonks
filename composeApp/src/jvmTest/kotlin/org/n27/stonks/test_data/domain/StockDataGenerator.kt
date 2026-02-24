package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock

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
    eps: Double? = 7.47,
    pe: Double? = 34.7215522245231,
    pb: Double? = 51.967537,
    earningsQuarterlyGrowth: Double? = 86.4,
    expectedEpsGrowth: Double? = 7.72,
    valuationFloor: Double? = 12.5,
    currentIntrinsicValue: Double? = 93.375,
    forwardIntrinsicValue: Double? = 100.58355,
    currency: String? = "USD",
    lastUpdated: Long? = 1768064114877,
    isWatchlisted: Boolean = false,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    pb = pb,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    expectedEpsGrowth = expectedEpsGrowth,
    valuationFloor = valuationFloor,
    currentIntrinsicValue = currentIntrinsicValue,
    forwardIntrinsicValue = forwardIntrinsicValue,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)
