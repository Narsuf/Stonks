package org.n27.stonks.presentation.home.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.n27.stonks.presentation.home.entities.HomeInteraction

@Composable
internal fun HomeEditGrowthBottomSheet(
    itemIndex: Int?,
    onAction: (action: HomeInteraction) -> Unit,
) {
    Text("Bottom sheet: $itemIndex")
}
