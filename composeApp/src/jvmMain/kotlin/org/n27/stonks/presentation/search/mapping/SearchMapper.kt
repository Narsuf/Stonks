package org.n27.stonks.presentation.search.mapping

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.domain.Stock
import org.n27.stonks.domain.domain.Stocks
import org.n27.stonks.presentation.search.entities.SearchState
import java.math.RoundingMode
import java.util.Currency

internal fun Stocks.toContent() = SearchState.Content(
    search = "Search ticker...",
    items = items.map { it.toPresentationEntity() }.toPersistentList(),
)

private fun Stock.toPresentationEntity() = SearchState.Content.Item(
    iconUrl = logoUrl,
    name = companyName,
    symbol = symbol,
    price = price.toString().toBigDecimal().setScale(2, RoundingMode.HALF_UP),
    currency = Currency.getInstance(currency),
)