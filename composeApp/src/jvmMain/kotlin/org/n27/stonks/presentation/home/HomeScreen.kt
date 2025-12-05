package org.n27.stonks.presentation.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.home.composables.HomeContent
import org.n27.stonks.presentation.home.composables.HomeEditGrowthBottomSheet
import org.n27.stonks.presentation.home.composables.HomeLoading
import org.n27.stonks.presentation.home.entities.HomeEvent.CloseBottomSheet
import org.n27.stonks.presentation.home.entities.HomeEvent.ShowBottomSheet
import org.n27.stonks.presentation.home.entities.HomeInteraction.Retry
import org.n27.stonks.presentation.home.entities.HomeState.*

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun HomeScreen(viewModel: HomeViewModel) {

    val state by viewModel.viewState.collectAsState()

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.viewEvent.collect { event ->
            when (event) {
                CloseBottomSheet -> {
                    selectedIndex = null
                    showBottomSheet = false
                }
                is ShowBottomSheet -> {
                    selectedIndex = event.index
                    showBottomSheet = true
                }
            }
        }
    }

    when (val s = state) {
        Idle -> Unit
        Loading -> HomeLoading()
        Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
        is Content -> HomeContent(s, viewModel::handleInteraction)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = modalSheetState,
            onDismissRequest = { showBottomSheet = false }
        ) { HomeEditGrowthBottomSheet(selectedIndex, state, viewModel::handleInteraction) }
    }
}
