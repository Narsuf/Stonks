package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.AddClicked
import org.n27.stonks.presentation.home.entities.HomeInteraction.SearchClicked
import org.n27.stonks.presentation.home.entities.HomeState.Content

@Composable
internal fun HomeContent(
    content: Content,
    onAction: (action: HomeInteraction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.default),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchButton(onAction)
        WatchlistSectionHeader(onAction)

        if (content.watchlist.isEmpty())
            EmptyWatchlist()
        else
           HomeWatchlistContent(content, onAction)
    }
}

@Composable
private fun SearchButton(onAction: (action: HomeInteraction) -> Unit) {
    Button(
        onClick = { onAction(SearchClicked) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.smaller),
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Search",
                modifier = Modifier.padding(end = Spacing.smallest)
            )
        }
    }
}

@Composable
private fun WatchlistSectionHeader(onAction: (action: HomeInteraction) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.default),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Watchlist",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = Spacing.smaller),
            )
            TextButton(
                onClick = { onAction(AddClicked) }
            ) { Text("Add") }
        }

        Text(
            text = "Expected EPS Growth",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = Spacing.smallest)
        )
    }
}

@Composable
private fun EmptyWatchlist() {
    Icon(
        imageVector = Icons.Default.StarBorder,
        contentDescription = "Empty Watchlist",
        tint = Color.Gray,
        modifier = Modifier
            .size(64.dp)
            .padding(bottom = Spacing.smaller)
    )
    Text(
        text = "Your watchlist is empty",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Gray,
    )
}
