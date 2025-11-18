package org.n27.stonks.presentation.search

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.search.entities.SearchInteraction
import org.n27.stonks.presentation.search.entities.SearchInteraction.Retry
import org.n27.stonks.presentation.search.entities.SearchState
import org.n27.stonks.presentation.search.entities.SearchState.*
import org.n27.stonks.presentation.search.mapping.toContent

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val state = MutableStateFlow<SearchState>(Idle)
    val viewState = state.asStateFlow()

    init { requestStocks() }

    internal fun handleInteraction(action: SearchInteraction) = when(action) {
        Retry -> requestStocks()
    }

    private fun requestStocks() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStocks().fold(
                onSuccess = { it.toContent() },
                onFailure = { Error }
            )

            state.emit(newState)
        }
    }
}