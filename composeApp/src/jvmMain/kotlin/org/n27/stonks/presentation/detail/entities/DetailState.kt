package org.n27.stonks.presentation.detail.entities

import kotlinx.collections.immutable.ImmutableList

internal sealed class DetailState {

    data object Idle : DetailState()
    data object Loading: DetailState()
    data object Error: DetailState()

    data class Content(
        val symbol: String,
        val logoUrl: String,
        val name: String,
        val price: String?,
        val cells: ImmutableList<Cell>,
    ) : DetailState() {

        data class Cell(
            val title: String,
            val value: String,
            val description: String,
        )
    }
}
