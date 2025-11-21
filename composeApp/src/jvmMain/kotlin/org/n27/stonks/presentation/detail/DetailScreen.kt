package org.n27.stonks.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.common.composables.TopBar
import org.n27.stonks.presentation.detail.composables.DetailContent
import org.n27.stonks.presentation.detail.composables.DetailLoading
import org.n27.stonks.presentation.detail.entities.DetailInteraction.GoBack
import org.n27.stonks.presentation.detail.entities.DetailInteraction.Retry
import org.n27.stonks.presentation.detail.entities.DetailState.*

@Composable
internal fun DetailScreen(viewModel: DetailViewModel) {

    val state by viewModel.viewState.collectAsState()

    Column {
        TopBar { viewModel.handleInteraction(GoBack) }

        when (val s = state) {
            Idle,
            Loading -> DetailLoading()
            Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
            is Content -> DetailContent(s)
        }
    }
}
