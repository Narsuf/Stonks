package org.n27.stonks.data.home

import org.n27.stonks.data.search.model.SearchStockRaw
import org.n27.stonks.domain.home.Home

internal fun List<SearchStockRaw>.toDomainEntity() = Home(
    items = map { it.toDomainEntity() }
)

private fun SearchStockRaw.toDomainEntity() = Home.Stock(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price,
    currency = currency,
    eps = eps,
)
