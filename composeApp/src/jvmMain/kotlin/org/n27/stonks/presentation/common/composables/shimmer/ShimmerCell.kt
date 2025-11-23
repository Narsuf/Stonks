package org.n27.stonks.presentation.common.composables.shimmer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ShimmerCell() {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ShimmerBone(
            width = 40.dp,
            height = 40.dp,
            shape = CircleShape
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            ShimmerBone(width = 180.dp, height = 18.dp)
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBone(width = 80.dp, height = 14.dp)
        }

        ShimmerBone(width = 70.dp, height = 18.dp)
    }
}
