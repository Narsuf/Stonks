package org.n27.stonks.presentation.search.entities

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.collections.immutable.ImmutableList

sealed class SearchState {

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
            val icon: ImageBitmap?,
            val name: String,
            val symbol: String,
        )
    }
}
