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
import org.n27.stonks.presentation.app.entities.AppState.*
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.detail.DetailScreen
import org.n27.stonks.presentation.home.HomeScreen
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

    MaterialTheme(AppColors.customColorScheme) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(Modifier.padding(padding)) {
                when (val s = state) {
                    is Home -> HomeScreen(s.viewModel)
                    is Search -> SearchScreen(s.viewModel)
                    is Detail -> DetailScreen(s.viewModel)
                }
            }
        }
    }
}
