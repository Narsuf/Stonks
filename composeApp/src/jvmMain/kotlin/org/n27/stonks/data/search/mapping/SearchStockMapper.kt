package org.n27.stonks.data.search.mapping

import org.n27.stonks.data.search.model.SearchStockRaw
import org.n27.stonks.domain.search.Search


internal fun List<SearchStockRaw>.toDomainEntity(pages: Int) = Search(
    pages = pages,
    items = map { it.toDomainEntity() }
)

private fun SearchStockRaw.toDomainEntity() = Search.Stock(
    symbol = symbol,
    logoUrl = logoUrl,
    companyName = companyName,
    price = price,
    currency = currency,
)
