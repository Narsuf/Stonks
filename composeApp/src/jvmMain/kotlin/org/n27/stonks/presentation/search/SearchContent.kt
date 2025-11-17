package org.n27.stonks.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.n27.stonks.presentation.common.Cell
import org.n27.stonks.presentation.common.RoundIcon
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.search.entities.SearchState.Content
import java.text.NumberFormat

@Composable
fun SearchContent(content: Content) {
    var search by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = search,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.default),
            placeholder = { Text(content.search) },
            singleLine = true,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.default)
        ) {
            items(content.items) { SearchCell(stock = it) }
        }
    }
}

@Composable
fun SearchCell(stock: Content.Item) {
    Cell(
        start = { RoundIcon(stock.iconUrl) },
        center = {
            Column {
                Text(text = stock.name, style = MaterialTheme.typography.titleMedium)
                Text(text = stock.symbol, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        },
        end = {
            val format = NumberFormat.getCurrencyInstance().apply { this.currency = stock.currency }
            val priceFormatted = format.format(stock.price)
            Text(text = priceFormatted, style = MaterialTheme.typography.bodyMedium)
        }
    )
}
