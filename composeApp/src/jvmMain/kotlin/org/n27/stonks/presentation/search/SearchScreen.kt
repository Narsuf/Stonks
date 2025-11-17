package org.n27.stonks.presentation.search

import androidx.compose.runtime.Composable
import org.n27.stonks.presentation.search.entities.SearchState

@Composable
fun SearchScreen(content: SearchState) {
    when (content) {
        is SearchState.Content -> SearchContent(content)
    }
}