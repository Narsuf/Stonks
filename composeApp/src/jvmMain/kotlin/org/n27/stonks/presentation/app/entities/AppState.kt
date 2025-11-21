package org.n27.stonks.presentation.app.entities

import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.detail.DetailViewModel
import org.n27.stonks.presentation.home.HomeViewModel
import org.n27.stonks.presentation.search.SearchViewModel

internal sealed class AppState {
    abstract val viewModel: ViewModel

    data class Home(override val viewModel: HomeViewModel) : AppState()
    data class Search(override val viewModel: SearchViewModel) : AppState()
    data class Detail(override val viewModel: DetailViewModel) : AppState()
}
