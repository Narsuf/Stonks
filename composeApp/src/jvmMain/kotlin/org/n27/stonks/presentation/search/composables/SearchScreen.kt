package org.n27.stonks.presentation.search.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.common.composables.TopBar
import org.n27.stonks.presentation.search.SearchViewModel
import org.n27.stonks.presentation.search.entities.SearchInteraction.BackClicked
import org.n27.stonks.presentation.search.entities.SearchInteraction.Retry
import org.n27.stonks.presentation.search.entities.SearchState.*

@Composable
internal fun SearchScreen(viewModel: SearchViewModel) {

    val state by viewModel.viewState.collectAsState()

    Column {
        TopBar(title = "Search") { viewModel.handleInteraction(BackClicked) }

        when (val s = state) {
            Idle -> Unit
            Loading -> SearchLoading()
            Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
            is Content -> SearchContent(content = s, onAction = viewModel::handleInteraction)
        }
    }
}
