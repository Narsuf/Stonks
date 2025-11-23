package org.n27.stonks.presentation.common.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val ShimmerColorShades = listOf(
    Color.LightGray.copy(0.9f),
    Color.LightGray.copy(0.2f),
    Color.LightGray.copy(0.9f)
)

fun Modifier.shimmerLoading(widthOfBones: Float = 1000f): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")

    val xShimmer by transition.animateFloat(
        initialValue = 0f,
        targetValue = widthOfBones,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "xShimmer"
    )

    val brush = Brush.linearGradient(
        colors = ShimmerColorShades,
        start = Offset(xShimmer, 0f),
        end = Offset(xShimmer + widthOfBones, 0f)
    )

    return@composed background(brush = brush)
}

fun <T> Modifier.ifNotNull(value: T?, block: Modifier.(T) -> Modifier): Modifier {
    return if (value != null) this.block(value) else this
}
