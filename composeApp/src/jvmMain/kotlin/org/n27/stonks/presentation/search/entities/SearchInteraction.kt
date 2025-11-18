package org.n27.stonks.presentation.search.entities

internal sealed class SearchInteraction {

    data object Retry : SearchInteraction()
}