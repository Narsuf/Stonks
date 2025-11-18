package org.n27.stonks.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import org.n27.stonks.presentation.search.entities.SearchState.*

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinInject(),
) {

    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }

    val state by viewModel.viewState.collectAsState()

    when (val s = state) {
        is Idle, Error -> Unit
        is Loading -> SearchLoading()
        is Content -> SearchContent(s)
    }
}