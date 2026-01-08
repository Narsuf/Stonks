package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.domain.models.Stocks.Stock

fun getStocks(
    items: List<Stock> = listOf(getStock()),
    nextPage: Int = 2,
) = Stocks(
    items = items,
    nextPage = nextPage,
)

fun getStock(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logo: String? = "/9j/2wCEAAEBAQEBAQEBAQEBAQEBAQEBAQEBA",
    price: Double = 285.910003662109,
    dividendYield: Double = 0.37,
    eps: Double = 7.47,
    pe: Double = 38.2744315478058,
    pb: Double = 51.430573,
    earningsQuarterlyGrowth: Double = 86.4,
    expectedEpsGrowth: Double? = 7.75,
    valuationFloor: Double? = 12.5,
    currentIntrinsicValue: Double = 23.31,
    forwardIntrinsicValue: Double? = 25.776198,
    currency: String = "USD",
    lastUpdated: Long? = 1767879314413,
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
)
