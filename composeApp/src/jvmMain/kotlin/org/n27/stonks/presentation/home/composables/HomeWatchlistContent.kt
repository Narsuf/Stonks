package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.Cell
import org.n27.stonks.presentation.common.composables.DeltaText
import org.n27.stonks.presentation.common.composables.RoundIcon
import org.n27.stonks.presentation.common.composables.shimmer.ShimmerOutlinedCell
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.*
import org.n27.stonks.presentation.home.entities.HomeState.Content

@Composable
internal fun HomeWatchlistContent(
    content: Content,
    onAction: (action: HomeInteraction) -> Unit,
) {
    val listState = rememberLazyListState()
    val lastVisibleItemIndex by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
    }

    LaunchedEffect(lastVisibleItemIndex, content.watchlist.size) {
        val buffer = 2
        if (lastVisibleItemIndex >= content.watchlist.lastIndex - buffer && !content.isPageLoading && !content.isEndReached) {
            onAction(LoadNextPage)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(Spacing.smaller),
        contentPadding = PaddingValues(bottom = Spacing.smaller),
    ) {
        itemsIndexed(
            items = content.watchlist,
            key = { _, item -> item.symbol },
        ) { index, item -> ListItem(index, item, onAction) }

        if (content.isPageLoading) item { ShimmerOutlinedCell(Modifier.fillMaxWidth(0.5f)) }
    }
}

@Composable
private fun ListItem(
    index: Int,
    item: Content.Item,
    onAction: (action: HomeInteraction) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        WatchlistCell(index, item, onAction)
        IconButton(
            onClick = { onAction(RemoveItemClicked(index)) }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete",
                tint = Color.Gray,
            )
        }

        IconButton(
            onClick = { onAction(EditItemClicked(index)) }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Gray,
            )
        }

        item.extraValue?.let {
            Spacer(Modifier.weight(1f))
            Text(
                text = it.asString(),
                modifier = Modifier.padding(start = Spacing.smallest),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun WatchlistCell(
    index: Int,
    item: Content.Item,
    onAction: (action: HomeInteraction) -> Unit,
) {
    Cell(
        start = { RoundIcon(item.icon) },
        center = {
            Column {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                Text(text = item.symbol, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        },
        end = {
            Column(horizontalAlignment = Alignment.End) {
                item.price?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                item.targetPrice?.let { DeltaText(it) }
            }
        },
        modifier = Modifier.fillMaxWidth(0.5f),
    ) { onAction(ItemClicked(index)) }
}
