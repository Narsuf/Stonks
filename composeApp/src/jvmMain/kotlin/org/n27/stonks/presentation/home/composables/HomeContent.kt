package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.PrimaryButton
import org.n27.stonks.presentation.common.composables.shimmer.ShimmerCellList
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.AddClicked
import org.n27.stonks.presentation.home.entities.HomeInteraction.SearchClicked
import org.n27.stonks.presentation.home.entities.HomeState.Content
import stonks.composeapp.generated.resources.*

@Composable
internal fun HomeContent(
    content: Content,
    onAction: (action: HomeInteraction) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = Spacing.default)
            .padding(top = Spacing.small),
    ) {
        PrimaryButton(
            title = stringResource(Res.string.search),
            icon = Icons.Default.Search,
        ) { onAction(SearchClicked) }
        WatchlistSectionHeader(onAction)

        when {
            content.isWatchlistLoading -> ShimmerCellList()
            content.watchlist.isEmpty() -> EmptyWatchlist()
            else -> HomeWatchlistContent(content, onAction)
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
            .padding(
                top = Spacing.small,
                bottom = Spacing.smallest,
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.watchlist),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = Spacing.smaller),
            )
            TextButton(
                onClick = { onAction(AddClicked) }
            ) { Text(stringResource(Res.string.add)) }
        }

        Text(
            text = stringResource(Res.string.valuation_floor_and_eps_growth),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = Spacing.smallest)
        )
    }
}

@Composable
private fun EmptyWatchlist() {
    Icon(
        imageVector = Icons.Default.StarBorder,
        contentDescription = stringResource(Res.string.empty_watchlist_description),
        tint = Color.Gray,
        modifier = Modifier
            .size(64.dp)
            .padding(bottom = Spacing.smaller)
    )
    Text(
        text = stringResource(Res.string.empty_watchlist),
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Gray,
    )
}
