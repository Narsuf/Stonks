package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.models.Stock
import org.n27.stonks.domain.models.Stocks

fun getStocks(
    items: List<Stock> = listOf(
        getStock(
            expectedEpsGrowth = 7.75,
            valuationFloor = 12.5,
            currentIntrinsicValue = 185.03,
            forwardIntrinsicValue = 177.83,
            lastUpdated = 1765627200000,
        )
    ),
    nextPage: Int = 2,
) = Stocks(
    items = items,
    nextPage = nextPage,
)

fun getStock(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logoUrl: String = "https://logo.clearbit.com/apple.com",
    price: Double = 285.910003662109,
    dividendYield: Double = 0.37,
    eps: Double = 7.47,
    pe: Double = 38.2744315478058,
    earningsQuarterlyGrowth: Double = 86.4,
    expectedEpsGrowth: Double? = null,
    valuationFloor: Double? = null,
    currentIntrinsicValue: Double = 93.375,
    forwardIntrinsicValue: Double? = null,
    currency: String = "USD",
    lastUpdated: Long? = null,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    expectedEpsGrowth = expectedEpsGrowth,
    valuationFloor = valuationFloor,
    currentIntrinsicValue = currentIntrinsicValue,
    forwardIntrinsicValue = forwardIntrinsicValue,
    currency = currency,
    lastUpdated = lastUpdated,
)
