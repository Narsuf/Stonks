package org.n27.stonks.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.search.entities.SearchInteraction.Retry
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
        Idle -> Unit
        Loading -> SearchLoading()
        is Content -> SearchContent(content = s, onAction = viewModel::handleInteraction)
        Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
    }
}
