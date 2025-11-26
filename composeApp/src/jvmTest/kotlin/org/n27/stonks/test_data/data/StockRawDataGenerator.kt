package org.n27.stonks.test_data.data

import org.n27.stonks.data.common.model.StockRaw

fun getStockRaw(
    symbol: String = "AAPL",
    logoUrl: String = "https://logo.clearbit.com/apple.com",
    companyName: String = "Apple Inc.",
    price: Double = 276.970001220703,
    eps: Double = 7.48,
    trailingPe: Double = 37.028076,
    forwardPe: Double = 33.329723,
    dividendYield: Double = 0.38,
    earningsQuarterlyGrowth: Double = 86.4,
    currency: String = "USD",
    intrinsicValue: Double = 174.284,
) = StockRaw(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price,
    eps = eps,
    trailingPe = trailingPe,
    forwardPe = forwardPe,
    dividendYield = dividendYield,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    currency = currency,
    intrinsicValue = intrinsicValue,
)
