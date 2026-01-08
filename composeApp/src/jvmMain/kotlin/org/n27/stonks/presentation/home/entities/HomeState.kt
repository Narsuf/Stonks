package org.n27.stonks.presentation.home.entities

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.collections.immutable.ImmutableList
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import java.math.BigDecimal

internal sealed class HomeState {

    data object Idle : HomeState()
    data object Loading: HomeState()
    data object Error: HomeState()

    data class Content(
        val bottomSheet: BottomSheet,
        val isWatchlistLoading: Boolean,
        val watchlist: ImmutableList<Item>,
        val isEndReached: Boolean,
        val isPageLoading: Boolean,
    ) : HomeState() {

        data class BottomSheet(
            val epsGrowthInput: BigDecimal,
            val valuationFloorInput: BigDecimal,
        )

        data class Item(
            val icon: ImageBitmap?,
            val name: String,
            val symbol: String,
            val price: String?,
            val targetPrice: DeltaTextEntity?,
            val extraValue: String?,
        )
    }
}
