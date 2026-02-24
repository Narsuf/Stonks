package org.n27.stonks.presentation.common.broadcast

import org.jetbrains.compose.resources.StringResource

sealed class Event {

    data class GoBack(val result: Map<String, Any>? = null) : Event()

    sealed class NavigateToSearch : Event() {
        data object All : NavigateToSearch()
        data object Watchlist : NavigateToSearch()
    }

    data class NavigateToDetail(val symbol: String) : Event()
    data class ShowErrorNotification(val title: StringResource) : Event()

    sealed class WatchlistEvent : Event() {
        data object AssetAdded : WatchlistEvent()
        data object AssetRemoved : WatchlistEvent()
    }
}
