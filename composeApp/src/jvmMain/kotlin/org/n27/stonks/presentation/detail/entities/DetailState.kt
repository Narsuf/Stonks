package org.n27.stonks.presentation.detail.entities

import kotlinx.collections.immutable.ImmutableList
import org.n27.stonks.presentation.common.composables.DeltaTextEntity

internal sealed class DetailState {

    data object Idle : DetailState()
    data object Loading: DetailState()
    data object Error: DetailState()

    data class Content(
        val symbol: String,
        val logoUrl: String,
        val name: String,
        val price: String?,
        val targetPrice: DeltaTextEntity?,
        val cells: ImmutableList<Cell>,
    ) : DetailState() {

        data class Cell(
            val title: String,
            val value: String,
            val description: String,
        )
    }
}
