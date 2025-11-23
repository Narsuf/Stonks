package org.n27.stonks.presentation.common.composables.shimmer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.extensions.shimmerLoading

@Composable
fun ShimmerBone(
    width: Dp = Dp.Unspecified,
    height: Dp,
    shape: Shape = RoundedCornerShape(4.dp),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .then(
                if (width != Dp.Unspecified)
                    Modifier.width(width)
                else
                    Modifier
            )
            .height(height)
            .clip(shape)
            .shimmerLoading()
    )
}
