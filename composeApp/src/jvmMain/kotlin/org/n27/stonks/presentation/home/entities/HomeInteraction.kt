package org.n27.stonks.presentation.home.entities

internal sealed class HomeInteraction {

    data object Retry : HomeInteraction()
    data object SearchClicked : HomeInteraction()
    data object AddClicked : HomeInteraction()
}
