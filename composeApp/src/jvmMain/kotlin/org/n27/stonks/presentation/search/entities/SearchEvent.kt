package org.n27.stonks.presentation.search.entities

internal sealed class SearchEvent {

    data class NavigateToDetail(val symbol: String) : SearchEvent()
    data class ShowErrorNotification(val title: String) : SearchEvent()
}
