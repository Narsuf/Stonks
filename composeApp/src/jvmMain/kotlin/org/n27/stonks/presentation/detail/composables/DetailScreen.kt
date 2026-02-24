package org.n27.stonks.presentation.detail.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.common.composables.ErrorScreen
import org.n27.stonks.presentation.common.composables.TopBar
import org.n27.stonks.presentation.detail.DetailViewModel
import org.n27.stonks.presentation.detail.entities.DetailInteraction.*
import org.n27.stonks.presentation.detail.entities.DetailState.*

@Composable
internal fun DetailScreen(viewModel: DetailViewModel) {

    val state by viewModel.viewState.collectAsState()

    Column {
        TopBar(
            onBack = { viewModel.handleInteraction(BackClicked) },
            rightButton = {
                (state as? Content)?.let { content ->
                    IconButton(onClick = { viewModel.handleInteraction(ToggleWatchlist) }) {
                        if (content.isWatchlisted) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "",
                                tint = AppColors.Yellow
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.StarOutline,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        )

        when (val s = state) {
            Idle,
            Loading -> DetailLoading()
            Error -> ErrorScreen { viewModel.handleInteraction(Retry) }
            is Content -> DetailContent(s)
        }
    }
}
