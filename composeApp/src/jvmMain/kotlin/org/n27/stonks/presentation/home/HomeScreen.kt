package org.n27.stonks.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.home.composables.HomeContent
import org.n27.stonks.presentation.home.entities.HomeInteraction.Retry
import org.n27.stonks.presentation.home.entities.HomeState.*

@Composable
internal fun HomeScreen(viewModel: HomeViewModel) {

    val state by viewModel.viewState.collectAsState()

    when (val s = state) {
        Idle, Loading -> Unit
        Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
        is Content -> HomeContent(s, viewModel::handleInteraction)
    }
}
