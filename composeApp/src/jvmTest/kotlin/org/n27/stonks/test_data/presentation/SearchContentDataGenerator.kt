package org.n27.stonks.test_data.presentation

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.presentation.search.entities.SearchState.Content

fun getSearchContent(
    search: String = "",
    isSearchLoading: Boolean = false,
    items: List<Content.Item> = listOf(getSearchContentItem()),
    isPageLoading: Boolean = false,
    isEndReached: Boolean = false,
) = Content(
    search = search,
    isSearchLoading = isSearchLoading,
    items = items.toPersistentList(),
    isPageLoading = isPageLoading,
    isEndReached = isEndReached,
)

fun getSearchContentItem(
    name: String = "Apple Inc.",
    symbol: String = "AAPL",
) = Content.Item(
    icon = null,
    name = name,
    symbol = symbol,
)
