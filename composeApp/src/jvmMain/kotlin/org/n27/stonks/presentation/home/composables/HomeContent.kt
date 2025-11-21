package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing

@Composable
internal fun HomeContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.default),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { },
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = Spacing.default),
        ) { WatchlistSectionHeader() }

        EmptyWatchlist()
    }
}

@Composable
private fun WatchlistSectionHeader() {
    Text(
        text = "Watchlist",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(end = Spacing.smaller),
    )
    TextButton(
        onClick = { }
    ) { Text("Add") }
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
