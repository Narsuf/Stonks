package org.n27.stonks.presentation.detail.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.Cell
import org.n27.stonks.presentation.common.composables.DeltaText
import org.n27.stonks.presentation.common.composables.RoundIcon
import org.n27.stonks.presentation.detail.entities.DetailState.Content

@Composable
internal fun DetailContent(content: Content) {
    Column(
        modifier = Modifier.padding(horizontal = Spacing.default)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RoundIcon(content.logoUrl)
            Spacer(Modifier.width(Spacing.small))
            Column {
                Text(text = content.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = content.symbol, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }

        content.price?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    top = Spacing.default,
                    bottom = Spacing.smallest,
                ),
            )
        }
        content.targetPrice?.let { DeltaText(it) }

        LazyColumn(
            modifier = Modifier.padding(top = Spacing.default),
            contentPadding = PaddingValues(bottom = Spacing.smaller),
            verticalArrangement = Arrangement.spacedBy(Spacing.default),
        ) {
            items(content.cells.chunked(2)) { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.default)) {
                    rowItems.forEach { cell ->
                        InfoCell(
                            cell = cell,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun InfoCell(
    cell: Content.Cell,
    modifier: Modifier = Modifier
) {
    Cell(
        center = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = cell.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = Spacing.smaller)
                    )
                    Text(
                        text = cell.value,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = Spacing.smaller)
                            .padding(start = Spacing.big)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) { cell.delta?.let { DeltaText(it) } }

                Text(
                    text = cell.description,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = Spacing.smaller)
                        .padding(horizontal = Spacing.smaller),
                )
            }
        },
        modifier = modifier,
    )
}
