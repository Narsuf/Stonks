package org.n27.stonks.test_data.data

import org.n27.stonks.data.yfinance.model.StockRaw

fun getStockRaw(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logoUrl: String = "https://logo.clearbit.com/apple.com",
    price: Double = 285.910003662109,
    dividendYield: Double = 0.37,
    eps: Double = 7.47,
    pe: Double = 38.2744315478058,
    earningsQuarterlyGrowth: Double = 86.4,
    intrinsicValue: Double = 93.375,
    currency: String = "USD",
) = StockRaw(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    intrinsicValue = intrinsicValue,
    currency = currency,
)
