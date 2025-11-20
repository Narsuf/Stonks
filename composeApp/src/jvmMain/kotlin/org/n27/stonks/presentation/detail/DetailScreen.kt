package org.n27.stonks.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.TopBar
import org.n27.stonks.presentation.detail.entities.DetailInteraction.GoBack

@Composable
internal fun DetailScreen(viewModel: DetailViewModel) {

    val state by viewModel.viewState.collectAsState()

    Column {
        TopBar { viewModel.handleInteraction(GoBack) }

        Spacer(Modifier.height(Spacing.default))

        Text("Detail")
    }
}
