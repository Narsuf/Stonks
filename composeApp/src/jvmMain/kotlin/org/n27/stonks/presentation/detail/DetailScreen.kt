package org.n27.stonks.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import org.n27.stonks.presentation.app.entities.AppInteraction
import org.n27.stonks.presentation.app.entities.AppInteraction.GoBack
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.TopBar

@Composable
internal fun DetailScreen(
    symbol: String,
    onAction: (action: AppInteraction) -> Unit,
) {

    val viewModel: DetailViewModel = koinInject { parametersOf(symbol) }

    val state by viewModel.viewState.collectAsState()

    Column {
        TopBar { onAction(GoBack) }

        Spacer(Modifier.height(Spacing.default))

        Text("Detail")
    }
}
