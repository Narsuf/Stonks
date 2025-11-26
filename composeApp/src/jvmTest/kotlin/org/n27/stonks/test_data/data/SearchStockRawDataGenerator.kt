package org.n27.stonks.test_data.data

import org.n27.stonks.data.search.model.SearchStockRaw

fun getSearchStockRaw(
    symbol: String = "AAPL",
    logoUrl: String = "https://logo.clearbit.com/apple.com",
    companyName: String = "Apple Inc.",
    price: Double = 276.970001220703,
    eps: Double = 7.48,
    currency: String = "USD",
) = SearchStockRaw(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price,
    currency = currency,
    eps = eps,
)
