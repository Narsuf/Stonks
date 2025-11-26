package org.n27.stonks.test_data.domain

import org.n27.stonks.domain.home.Home
import org.n27.stonks.domain.home.Home.Stock

fun getHome(
    items: List<Stock> = listOf(getHomeStock()),
) = Home(
    items = items,
)

fun getHomeStock(
    symbol: String = "AAPL",
    logoUrl: String = "https://logo.clearbit.com/apple.com",
    companyName: String = "Apple Inc.",
    price: Double = 276.970001220703,
    eps: Double = 7.48,
) = Stock(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price,
    currency = "USD",
    eps = eps,
)
