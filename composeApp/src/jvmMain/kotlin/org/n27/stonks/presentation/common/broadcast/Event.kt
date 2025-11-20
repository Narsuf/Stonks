package org.n27.stonks.presentation.common.broadcast

sealed class Event {

    data object GoBack : Event()
    data class NavigateToDetail(val symbol: String) : Event()
    data class ShowErrorNotification(val title: String) : Event()
}
