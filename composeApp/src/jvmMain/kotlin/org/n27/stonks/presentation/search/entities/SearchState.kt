package org.n27.stonks.presentation.search.entities

import kotlinx.collections.immutable.ImmutableList

internal sealed class SearchState {

    data object Idle: SearchState()
    data object Loading: SearchState()
    data object Error: SearchState()
    data class Content(
        val search: String,
        val isSearchLoading: Boolean,
        val items: ImmutableList<Item>,
        val isPageLoading: Boolean,
        val isEndReached: Boolean,
    ) : SearchState() {

        data class Item(
            val iconUrl: String,
            val name: String,
            val symbol: String,
            val price: String?,
        )
    }
}
