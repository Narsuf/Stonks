package org.n27.stonks.presentation.app

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.n27.stonks.presentation.app.entities.AppInteraction
import org.n27.stonks.presentation.app.entities.AppInteraction.GoBack
import org.n27.stonks.presentation.app.entities.AppInteraction.NavigateToDetail
import org.n27.stonks.presentation.app.entities.AppState
import org.n27.stonks.presentation.app.entities.AppState.Detail
import org.n27.stonks.presentation.app.entities.AppState.Home
import org.n27.stonks.presentation.common.ViewModel

@OptIn(FlowPreview::class)
class AppViewModel() : ViewModel() {

    private val state = MutableStateFlow<AppState>(Home)
    internal val viewState = state.asStateFlow()

    internal fun handleInteraction(action: AppInteraction) {
        state.value = when (action) {
            is NavigateToDetail -> Detail
            GoBack -> Home
        }
    }
}
