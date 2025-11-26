package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.search.Search

fun getSearch(
    pages: Int = 1,
    items: List<Search.Stock> = listOf(getSearchStock()),
) = Search(
    pages = pages,
    items = items,
)

fun getSearchStock(
    symbol: String = "AAPL",
    logoUrl: String = "https://logo.clearbit.com/apple.com",
    companyName: String = "Apple Inc.",
    price: Double = 276.970001220703,
    currency: String = "USD",
) = Search.Stock(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price,
    currency = currency
)
