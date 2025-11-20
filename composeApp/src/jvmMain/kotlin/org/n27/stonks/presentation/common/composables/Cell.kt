package org.n27.stonks.presentation.common.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing

@Composable
fun Cell(
    center: @Composable () -> Unit,
    end: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    start: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .then(onClick?.let { Modifier.clickable(onClick = it) } ?: Modifier)
            .padding(Spacing.small),
    ) {
        Row {
            start?.let {
                it()
                Spacer(Modifier.width(Spacing.small))
            }
            center()
            Spacer(Modifier.width(Spacing.small))
        }
        end()
    }
}
