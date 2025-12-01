package org.n27.stonks.presentation.common.composables.shimmer

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing

@Composable
fun ShimmerCellList() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.default)
    ) {
        repeat(12) {
            ShimmerCell()
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
