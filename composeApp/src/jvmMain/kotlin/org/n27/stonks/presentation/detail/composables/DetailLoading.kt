package org.n27.stonks.presentation.detail.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.ShimmerBone

@Composable
internal fun DetailLoading() {
    Column(modifier = Modifier.fillMaxWidth().padding(Spacing.default)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ShimmerBone(
                width = Spacing.biggest,
                height = Spacing.biggest,
                shape = CircleShape
            )
            Spacer(Modifier.width(Spacing.small))
            Column {
                ShimmerBone(width = 150.dp, height = 20.dp)
                Spacer(Modifier.height(Spacing.smaller))
                ShimmerBone(width = 80.dp, height = 16.dp)
            }
        }

        Spacer(Modifier.height(Spacing.default))
        ShimmerBone(width = 120.dp, height = 28.dp)
        Spacer(Modifier.height(Spacing.default))

        val placeholderCells = List(6) { it }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Spacing.default),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(placeholderCells.chunked(2)) { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.default),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { _ ->
                        ShimmerBone(
                            modifier = Modifier.weight(1f),
                            height = 60.dp
                        )
                    }
                }
            }
        }
    }
}
