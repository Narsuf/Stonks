package org.n27.stonks.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import org.n27.stonks.presentation.search.entities.SearchState
import org.n27.stonks.presentation.search.entities.SearchState.Idle
import org.n27.stonks.presentation.search.entities.SearchState.Loading

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinInject(),
) {

    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }

    val state by viewModel.viewState.collectAsState()

    when (val s = state) {
        is Idle, Loading -> Unit
        is SearchState.Content -> SearchContent(s)
    }
}