package org.n27.stonks.presentation.detail.entities

internal sealed class DetailInteraction {

    data object GoBack : DetailInteraction()
    data object Retry : DetailInteraction()
}

