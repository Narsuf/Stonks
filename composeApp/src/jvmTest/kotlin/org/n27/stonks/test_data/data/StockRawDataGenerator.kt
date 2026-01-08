package org.n27.stonks.test_data.data

import org.n27.stonks.data.yfinance.model.StockRaw

fun getStockRaw(
    symbol: String = "AAPL",
    companyName: String = "Apple Inc.",
    logoUrl: String = "https://logo.clearbit.com/apple.com",
    logo: String? = null,
    price: Double = 285.910003662109,
    dividendYield: Double = 0.37,
    eps: Double = 7.47,
    pe: Double = 38.2744315478058,
    pb: Double = 1.0,
    earningsQuarterlyGrowth: Double = 86.4,
    intrinsicValue: Double = 93.375,
    currency: String = "USD",
) = StockRaw(
    symbol = symbol,
    companyName = companyName,
    logoUrl = logoUrl,
    logo = logo,
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    pb = pb,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    intrinsicValue = intrinsicValue,
    currency = currency,
)
