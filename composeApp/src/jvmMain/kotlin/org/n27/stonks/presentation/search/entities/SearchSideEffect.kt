package org.n27.stonks.presentation.search.entities

internal sealed class SearchSideEffect {

    data class NavigateToDetail(val symbol: String) : SearchSideEffect()
    data class ShowErrorNotification(val title: String) : SearchSideEffect()
}
