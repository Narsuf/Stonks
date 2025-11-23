package org.n27.stonks.presentation.common.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeltaText(deltaTextEntity: DeltaTextEntity) = with(deltaTextEntity) {
    val color = when (state) {
        DeltaState.POSITIVE -> Color(0xFF37A931)
        DeltaState.NEGATIVE -> Color(0xFFD13D3D)
        DeltaState.NEUTRAL  -> Color.Gray
    }

    val triangle = when (state) {
        DeltaState.POSITIVE -> "▲"
        DeltaState.NEGATIVE -> "▼"
        DeltaState.NEUTRAL  -> ""
    }

    Text(
        text = "$triangle  $value · $percentage",
        style = MaterialTheme.typography.bodyMedium,
        color = color,
    )
}

data class DeltaTextEntity(
    val value: String,
    val percentage: String,
    val state: DeltaState,
)

enum class DeltaState {
    POSITIVE, NEGATIVE, NEUTRAL
}
