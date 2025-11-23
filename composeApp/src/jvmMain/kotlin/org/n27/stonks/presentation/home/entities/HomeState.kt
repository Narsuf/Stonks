package org.n27.stonks.presentation.home.entities

import kotlinx.collections.immutable.ImmutableList
import java.math.BigDecimal

internal sealed class HomeState {

    data object Idle : HomeState()
    data object Loading: HomeState()
    data object Error: HomeState()

    data class Content(
        val input: BigDecimal,
        val isWatchlistLoading: Boolean,
        val watchlist: ImmutableList<Item>,
        val isEndReached: Boolean,
        val isPageLoading: Boolean,
    ) : HomeState() {
        data class Item(
            val iconUrl: String,
            val name: String,
            val symbol: String,
            val price: String?,
            val estimatedEpsGrowth: String?,
        )
    }
}
