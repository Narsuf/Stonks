package org.n27.stonks.presentation.search.entities

sealed class SearchInteraction {

    data object Retry : SearchInteraction()
    data object LoadNextPage : SearchInteraction()
    data class SearchValueChanged(val text: String) : SearchInteraction()
}
