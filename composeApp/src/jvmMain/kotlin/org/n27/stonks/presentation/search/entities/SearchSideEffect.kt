package org.n27.stonks.presentation.search.entities

sealed class SearchSideEffect {

    data class ShowErrorNotification(val title: String) : SearchSideEffect()
}
