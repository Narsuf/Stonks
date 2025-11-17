package org.n27.stonks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.n27.stonks.presentation.search.SearchScreen
import org.n27.stonks.presentation.search.entities.SearchState
import java.math.BigDecimal
import java.util.Currency

@Composable
@Preview
fun App() {
    MaterialTheme {
        val content = SearchState.Content(
            search = "Search ticker...",
            items = persistentListOf(
                SearchState.Content.Item
                    (
                    iconUrl = "https://logo.clearbit.com/microsoft.com",
                    name = "Microsoft Corporation",
                    symbol = "MSFT",
                    price = BigDecimal("127.45"),
                    currency = Currency.getInstance("USD"),
                ),
                SearchState.Content.Item(
                    iconUrl = "https://logo.clearbit.com/apple.com",
                    name = "Apple Inc.",
                    symbol = "AAPL",
                    price = BigDecimal("267.77"),
                    currency = Currency.getInstance("USD"),
                )
            )
        )

        SearchScreen(content)
    }
}