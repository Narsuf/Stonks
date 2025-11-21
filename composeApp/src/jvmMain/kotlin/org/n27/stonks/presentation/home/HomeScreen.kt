package org.n27.stonks.presentation.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.home.composables.HomeContent
import org.n27.stonks.presentation.home.composables.HomeEditGrowthBottomSheet
import org.n27.stonks.presentation.home.entities.HomeEvent.ShowBottomSheet
import org.n27.stonks.presentation.home.entities.HomeInteraction.Retry
import org.n27.stonks.presentation.home.entities.HomeState.*

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(viewModel: HomeViewModel) {

    val state by viewModel.viewState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.viewEvent.collect { event ->
            when (event) {
                is ShowBottomSheet -> {
                    selectedIndex = event.index
                    showBottomSheet = true
                }
            }
        }
    }

    when (val s = state) {
        Idle, Loading -> Unit
        Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
        is Content -> HomeContent(s, viewModel::handleInteraction)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = modalSheetState,
            onDismissRequest = { showBottomSheet = false }
        ) { HomeEditGrowthBottomSheet(selectedIndex, viewModel::handleInteraction) }
    }
}
