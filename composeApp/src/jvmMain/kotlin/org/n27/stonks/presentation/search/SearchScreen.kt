package org.n27.stonks.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.search.composables.SearchContent
import org.n27.stonks.presentation.search.composables.SearchLoading
import org.n27.stonks.presentation.search.entities.SearchInteraction.Retry
import org.n27.stonks.presentation.search.entities.SearchState.*

@Composable
internal fun SearchScreen(viewModel: SearchViewModel) {

    val state by viewModel.viewState.collectAsState()

    when (val s = state) {
        Idle -> Unit
        Loading -> SearchLoading()
        Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
        is Content -> SearchContent(content = s, onAction = viewModel::handleInteraction)
    }
}
