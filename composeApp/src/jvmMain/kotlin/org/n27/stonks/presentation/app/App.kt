package org.n27.stonks.presentation.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.n27.stonks.presentation.app.entities.AppState.Detail
import org.n27.stonks.presentation.app.entities.AppState.Search
import org.n27.stonks.presentation.detail.DetailScreen
import org.n27.stonks.presentation.search.SearchScreen

@Composable
@Preview
fun App(viewModel: AppViewModel = koinInject()) {

    val state by viewModel.viewState.collectAsState()

    MaterialTheme {
        when (state) {
            Search -> SearchScreen(onAction = viewModel::handleInteraction)
            Detail -> DetailScreen()
        }
    }
}
