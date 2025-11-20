package org.n27.stonks.presentation.app.entities

sealed class AppInteraction {

    data object GoBack : AppInteraction()
    data class NavigateToDetail(val symbol: String) : AppInteraction()
}
