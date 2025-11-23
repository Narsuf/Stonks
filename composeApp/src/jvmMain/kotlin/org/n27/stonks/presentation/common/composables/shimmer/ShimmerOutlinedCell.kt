package org.n27.stonks.presentation.common.composables.shimmer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing

@Composable
fun ShimmerOutlinedCell(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(bottom = Spacing.smaller)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = Spacing.small,
                vertical = Spacing.smallest,
            ),
    ) { ShimmerCell() }
}
