package org.n27.stonks.presentation.common.broadcast

import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch.Origin.HOME

sealed class Event {
    data class GoBack(val result: String? = null) : Event()
    data class NavigateToSearch(val from: Origin = HOME) : Event() {
        enum class Origin { HOME, WATCHLIST }
    }
    data class NavigateToDetail(val symbol: String) : Event()
    data class ShowErrorNotification(val title: String) : Event()
}
