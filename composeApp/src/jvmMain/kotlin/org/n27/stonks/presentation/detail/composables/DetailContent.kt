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

        content.dividendYield?.let { InfoCell("Dividend", it) }
        Spacer(Modifier.height(Spacing.default))
        content.trailingPe?.let { InfoCell("Trailing P/E", it) }
        Spacer(Modifier.height(Spacing.smallest))
        content.forwardPe?.let { InfoCell("Forward P/E", it) }
        Spacer(Modifier.height(Spacing.default))
        content.eps?.let { InfoCell("EPS", it) }
        Spacer(Modifier.height(Spacing.default))
        content.earningsQuarterlyGrowth?.let { InfoCell("Growth", it) }
        Spacer(Modifier.height(Spacing.default))
        content.intrinsicValue?.let { InfoCell("Intrinsic value", it) }
    }
}

@Composable
private fun InfoCell(title: String, value: String, description: String? = null) {
    Cell(
        center = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = Spacing.smaller)
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = Spacing.smaller)
                            .padding(start = Spacing.big)
                    )
                }
                description?.let {
                    Text(
                        text = it,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(
                            top = Spacing.smaller,
                            start = Spacing.smaller,
                        ),
                    )
                }
            }
        },
    )
}
