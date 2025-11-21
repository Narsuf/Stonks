package org.n27.stonks.presentation.detail.entities

internal sealed class DetailInteraction {

    data object BackClicked : DetailInteraction()
    data object Retry : DetailInteraction()
}

