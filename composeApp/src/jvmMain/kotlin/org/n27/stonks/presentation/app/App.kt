package org.n27.stonks.presentation.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.n27.stonks.presentation.app.entities.AppEvent.ShowErrorNotification
import org.n27.stonks.presentation.app.entities.AppState.Detail
import org.n27.stonks.presentation.app.entities.AppState.Home
import org.n27.stonks.presentation.detail.DetailScreen
import org.n27.stonks.presentation.search.SearchScreen

@Composable
@Preview
fun App(viewModel: AppViewModel = koinInject()) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.viewEvent.collect { event ->
            when (event) {
                is ShowErrorNotification -> snackbarHostState.showSnackbar(event.title)
            }
        }
    }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(Modifier.padding(padding)) {
                when (val s = state) {
                    Home -> SearchScreen()
                    is Detail -> DetailScreen(s.symbol)
                }
            }
        }
    }
}
