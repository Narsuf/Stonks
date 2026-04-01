package org.n27.stonks.presentation.detail.entities

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs

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
        val items: ImmutableList<Item>,
        val isWatchlisted: Boolean = false,
    ) : DetailState() {

        sealed class Item {
            data class Header(val title: StringResource) : Item()
            data class CellPair(val first: Cell, val second: Cell? = null) : Item()
        }

        data class Cell(
            val title: StringResource,
            val value: String,
            val description: StringResourceWithArgs,
            val delta: DeltaTextEntity?,
            val color: Color? = null,
        )
    }
}
