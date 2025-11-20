package org.n27.stonks.presentation.search.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.Cell
import org.n27.stonks.presentation.common.composables.RoundIcon
import org.n27.stonks.presentation.search.entities.SearchInteraction
import org.n27.stonks.presentation.search.entities.SearchInteraction.*
import org.n27.stonks.presentation.search.entities.SearchState.Content

@Composable
internal fun SearchContent(
    content: Content,
    onAction: (action: SearchInteraction) -> Unit,
) {
    var search by remember(content.search) { mutableStateOf(content.search) }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = search,
            onValueChange = { onAction(SearchValueChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.default),
            placeholder = { Text("Search ticker...") },
            singleLine = true,
        )

        if (content.isSearchLoading)
            SearchCellListShimmer()
        else
            StockList(content, onAction)
    }
}

@Composable
private fun StockList(
    content: Content,
    onAction: (action: SearchInteraction) -> Unit,
) {
    val listState = rememberLazyListState()
    val lastVisibleItemIndex by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
    }

    LaunchedEffect(lastVisibleItemIndex, content.items.size) {
        val buffer = 2
        if (lastVisibleItemIndex >= content.items.lastIndex - buffer && !content.isPageLoading && !content.isEndReached) {
            onAction(LoadNextPage)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.default),
    ) {
        itemsIndexed(
            items = content.items,
            key = { _, item -> item.symbol },
        ) { index, item ->
           SearchCell(index, item, onAction)
        }

        if (content.isPageLoading) item { EmptyCell() }
    }
}

@Composable
private fun SearchCell(
    index: Int,
    stock: Content.Item,
    onAction: (action: SearchInteraction) -> Unit,
) {
    Cell(
        start = { RoundIcon(stock.iconUrl) },
        center = {
            Column {
                Text(text = stock.name, style = MaterialTheme.typography.titleMedium)
                Text(text = stock.symbol, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        },
        end = {
            stock.price?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
        },
        modifier = Modifier
            .padding(bottom = Spacing.smaller)
            .fillMaxWidth(),
    ) { onAction(ItemClicked(index)) }
}

@Composable
private fun EmptyCell() {
    Box(
        modifier = Modifier
            .padding(bottom = Spacing.smaller)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = Spacing.small,
                vertical = Spacing.smallest,
            ),
    ) { SearchCellShimmer() }
}
