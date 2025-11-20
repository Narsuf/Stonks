package org.n27.stonks.presentation.app.entities

import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.detail.DetailViewModel
import org.n27.stonks.presentation.search.SearchViewModel

internal sealed class AppState {
    object Idle : AppState()

    sealed class Screen : AppState() {
        abstract val viewModel: ViewModel

        data class Search(override val viewModel: SearchViewModel) : Screen()
        data class Detail(override val viewModel: DetailViewModel) : Screen()
    }
}
