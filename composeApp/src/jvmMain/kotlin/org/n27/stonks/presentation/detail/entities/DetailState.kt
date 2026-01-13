package org.n27.stonks.presentation.detail.entities

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.presentation.common.composables.DeltaTextEntity

sealed class DetailState {

    data object Idle : DetailState()
    data object Loading: DetailState()
    data object Error: DetailState()

    data class Content(
        val symbol: String,
        val icon: ImageBitmap?,
        val name: String,
        val price: String?,
        val lastUpdated: String?,
        val cells: ImmutableList<Cell>,
    ) : DetailState() {

        data class Cell(
            val title: StringResource,
            val value: String,
            val description: StringResource,
            val delta: DeltaTextEntity?,
        )
    }
}
