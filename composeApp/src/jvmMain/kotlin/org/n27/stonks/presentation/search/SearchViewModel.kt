package org.n27.stonks.presentation.search

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.search.entities.SearchInteraction
import org.n27.stonks.presentation.search.entities.SearchInteraction.LoadNextPage
import org.n27.stonks.presentation.search.entities.SearchInteraction.Retry
import org.n27.stonks.presentation.search.entities.SearchInteraction.SearchValueChanged
import org.n27.stonks.presentation.search.entities.SearchSideEffect
import org.n27.stonks.presentation.search.entities.SearchSideEffect.ShowErrorNotification
import org.n27.stonks.presentation.search.entities.SearchState
import org.n27.stonks.presentation.search.entities.SearchState.*
import org.n27.stonks.presentation.search.mapping.toContent
import org.n27.stonks.presentation.search.mapping.toPresentationEntity

@OptIn(FlowPreview::class)
class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val state = MutableStateFlow<SearchState>(Idle)
    internal val viewState = state.asStateFlow()

    private val sideEffect = Channel<SearchSideEffect>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewSideEffect = sideEffect.receiveAsFlow()

    private var maxPages = Int.MAX_VALUE
    private var currentPage = 0
    private val pageSize = 11

    private val searchText = MutableStateFlow<String?>(null)
    private var searchJob: Job? = null
    private var moreStocksJob: Job? = null

    private val onFailure: (Throwable) -> Unit = {
        viewModelScope.launch {
            if (it !is CancellationException)
                sideEffect.send(ShowErrorNotification("Something went wrong."))
            state.updateIfType { c: Content -> c.copy(isPageLoading = false) }
        }
    }

    init {
        requestInitialStocks()
        viewModelScope.launch {
            searchText
                .debounce(500)
                .distinctUntilChanged()
                .filterNotNull()
                .collect {
                    searchJob?.cancelAndJoin()
                    searchJob = viewModelScope.launch { performSearch(it) }
                }
        }
    }

    internal fun handleInteraction(action: SearchInteraction) = when(action) {
        Retry -> requestInitialStocks()
        LoadNextPage -> requestMoreStocks()
        is SearchValueChanged -> onSearchChanged(action.text)
    }

    private fun requestInitialStocks() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStocks(currentPage, pageSize).fold(
                onSuccess = {
                    currentPage += pageSize
                    maxPages = it.pages
                    it.toContent(isEndReached())
                },
                onFailure = { Error }
            )

            state.emit(newState)
        }
    }

    private fun requestMoreStocks() {
        moreStocksJob?.cancel()
        moreStocksJob = viewModelScope.launch {
            state.updateIfType { c: Content -> c.copy(isPageLoading = true) }

            repository.getStocks(currentPage, pageSize, searchText.value?.uppercase()).fold(
                onSuccess = {
                    currentPage += pageSize
                    state.updateIfType { c: Content ->
                        c.copy(
                            items = (c.items + it.items.toPresentationEntity()).toPersistentList(),
                            isPageLoading = false,
                            isEndReached = isEndReached(),
                        )
                    }
                },
                onFailure = onFailure,
            )
        }
    }

    private fun onSearchChanged(text: String) {
        searchText.value = text
        state.updateIfType { c: Content -> c.copy(search = text) }
    }

    private suspend fun performSearch(text: String) {
        moreStocksJob?.cancelAndJoin()
        state.updateIfType { c: Content ->
            c.copy(
                isSearchLoading = true,
                items = persistentListOf(),
                isPageLoading = false,
            )
        }
        currentPage = 0
        repository.getStocks(currentPage, pageSize, text.uppercase()).fold(
            onSuccess = {
                currentPage += pageSize
                maxPages = it.pages
                state.updateIfType { c: Content ->
                    c.copy(
                        isSearchLoading = false,
                        items = (c.items + it.items.toPresentationEntity()).toPersistentList(),
                        isEndReached = isEndReached(),
                    )
                }
            },
            onFailure = onFailure,
        )
    }

    private fun isEndReached() = currentPage >= maxPages
}
