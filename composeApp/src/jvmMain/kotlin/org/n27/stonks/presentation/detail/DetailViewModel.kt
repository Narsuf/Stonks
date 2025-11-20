package org.n27.stonks.presentation.detail

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.detail.entities.DetailState
import org.n27.stonks.presentation.detail.entities.DetailState.*
import org.n27.stonks.presentation.detail.mapping.toDetailContent

class DetailViewModel(
    private val symbol: String,
    private val repository: Repository,
) : ViewModel() {

    private val state = MutableStateFlow<DetailState>(Idle)
    internal val viewState = state.asStateFlow()

    init { requestStock() }

    private fun requestStock() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStock(symbol).fold(
                onSuccess = { it.toDetailContent() },
                onFailure = { Error }
            )

            state.emit(newState)
        }
    }
}
