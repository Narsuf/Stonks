package org.n27.stonks.presentation.detail.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.Cell
import org.n27.stonks.presentation.common.composables.RoundIcon
import org.n27.stonks.presentation.detail.entities.DetailState.Content

@Composable
internal fun DetailContent(content: Content) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RoundIcon(content.logoUrl)
        Spacer(Modifier.width(Spacing.small))
        Column {
            Text(text = content.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = content.symbol, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }

    Column(Modifier.padding(Spacing.default)) {
        content.price?.let { Text(text = it, style = MaterialTheme.typography.titleLarge) }
        Spacer(Modifier.height(Spacing.default))

        content.dividendYield?.let {
            Cell(
                center = {
                    Text(
                        text = "Dividend",
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                end = { Text(text = it, style = MaterialTheme.typography.titleMedium) }
            )
        }
    }
}
