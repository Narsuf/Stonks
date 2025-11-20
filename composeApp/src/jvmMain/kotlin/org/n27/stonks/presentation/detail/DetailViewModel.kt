package org.n27.stonks.presentation.detail

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.detail.entities.DetailState
import org.n27.stonks.presentation.detail.entities.DetailState.Idle

class DetailViewModel(private val repository: Repository) : ViewModel() {

    private val state = MutableStateFlow<DetailState>(Idle)
    internal val viewState = state.asStateFlow()
}
