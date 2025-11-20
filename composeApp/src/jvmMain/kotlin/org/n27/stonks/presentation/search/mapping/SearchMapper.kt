package org.n27.stonks.presentation.search.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.search.Search
import org.n27.stonks.domain.search.Search.Stock
import org.n27.stonks.presentation.common.extensions.toFormattedBigDecimal
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.common.extensions.truncateAfterDoubleSpace
import org.n27.stonks.presentation.search.entities.SearchState.Content

internal fun Search.toContent(isEndReached: Boolean) = Content(
    search = "",
    isSearchLoading = false,
    items = items.toPresentationEntity(),
    isPageLoading = false,
    isEndReached = isEndReached,
)

internal fun List<Stock>.toPresentationEntity(): ImmutableList<Content.Item> = map { it.toPresentationEntity() }
    .toPersistentList()

private fun Stock.toPresentationEntity() = Content.Item(
    iconUrl = logoUrl ?: "",
    name = companyName.truncateAfterDoubleSpace(),
    symbol = symbol,
    price = price?.toFormattedBigDecimal()?.toPrice(currency),
)
