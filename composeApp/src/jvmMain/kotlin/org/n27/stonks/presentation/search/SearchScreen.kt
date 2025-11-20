package org.n27.stonks.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.n27.stonks.presentation.app.entities.AppInteraction
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.search.entities.SearchInteraction.Retry
import org.n27.stonks.presentation.search.entities.SearchSideEffect.NavigateToDetail
import org.n27.stonks.presentation.search.entities.SearchSideEffect.ShowErrorNotification
import org.n27.stonks.presentation.search.entities.SearchState.*

@Composable
internal fun SearchScreen(
    viewModel: SearchViewModel = koinInject(),
    onAction: (action: AppInteraction) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    DisposableEffect(viewModel) {
        onDispose { viewModel.clear() }
    }

    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.viewSideEffect.collect { event ->
            when (event) {
                is NavigateToDetail -> onAction(AppInteraction.NavigateToDetail(event.symbol))
                is ShowErrorNotification -> {
                    scope.launch { snackbarHostState.showSnackbar(event.title) }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            when (val s = state) {
                Idle -> Unit
                Loading -> SearchLoading()
                is Content -> SearchContent(content = s, onAction = viewModel::handleInteraction)
                Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
            }
        }
    }
}
