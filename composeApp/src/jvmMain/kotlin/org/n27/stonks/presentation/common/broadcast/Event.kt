package org.n27.stonks.presentation.common.broadcast

import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch.Origin.HOME
import org.n27.stonks.presentation.detail.DetailParams

sealed class Event {
    data class GoBack(val result: Map<String, Any>? = null) : Event()
    data class NavigateToSearch(val from: Origin = HOME) : Event() {
        enum class Origin { HOME, WATCHLIST }
    }
    data class NavigateToDetail(val params: DetailParams) : Event()
    data class ShowErrorNotification(val title: String) : Event()
}
