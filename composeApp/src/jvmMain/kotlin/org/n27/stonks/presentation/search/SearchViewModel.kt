package org.n27.stonks.presentation.search

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.search.entities.SearchInteraction
import org.n27.stonks.presentation.search.entities.SearchInteraction.LoadNextPage
import org.n27.stonks.presentation.search.entities.SearchInteraction.Retry
import org.n27.stonks.presentation.search.entities.SearchState
import org.n27.stonks.presentation.search.entities.SearchState.*
import org.n27.stonks.presentation.search.mapping.toContent
import org.n27.stonks.presentation.search.mapping.toPresentationEntity

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val state = MutableStateFlow<SearchState>(Idle)
    val viewState = state.asStateFlow()

    private var currentPage = 0
    private val pageSize = 11

    init { requestStocks() }

    internal fun handleInteraction(action: SearchInteraction) = when(action) {
        Retry -> requestStocks()
        LoadNextPage -> requestMoreStocks()
    }

    private fun requestStocks() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStocks(currentPage, pageSize).fold(
                onSuccess = {
                    currentPage += pageSize
                    it.toContent()
                },
                onFailure = { Error }
            )

            state.emit(newState)
        }
    }

    private fun requestMoreStocks() {
        viewModelScope.launch {
            state.updateIfType { c: Content -> c.copy(isPageLoading = true) }

            repository.getStocks(currentPage, pageSize).fold(
                onSuccess = {
                    currentPage += pageSize
                    state.updateIfType { c: Content ->
                        c.copy(
                            items = (c.items + it.items.toPresentationEntity()).toPersistentList(),
                            isPageLoading = false,
                        )
                    }
                },
                onFailure = {
                    state.updateIfType { c: Content -> c.copy(isPageLoading = false) }
                }
            )
        }
    }
}
