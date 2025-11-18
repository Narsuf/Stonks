package org.n27.stonks.presentation.search.entities

import kotlinx.collections.immutable.ImmutableList
import java.math.BigDecimal
import java.util.Currency

sealed class SearchState {

    data object Idle: SearchState()
    data object Loading: SearchState()
    data object Error: SearchState()
    data class Content(
        val search: String,
        val items: ImmutableList<Item>,
        val isPageLoading: Boolean,
    ) : SearchState() {

        data class Item(
            val iconUrl: String,
            val name: String,
            val symbol: String,
            val price: String?,
        )
    }
}
