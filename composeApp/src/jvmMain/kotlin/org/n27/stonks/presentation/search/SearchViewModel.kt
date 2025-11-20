package org.n27.stonks.presentation.search

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.domain.Stocks
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.search.entities.SearchInteraction
import org.n27.stonks.presentation.search.entities.SearchInteraction.*
import org.n27.stonks.presentation.search.entities.SearchSideEffect
import org.n27.stonks.presentation.search.entities.SearchSideEffect.NavigateToDetail
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

    private lateinit var currentStocks: Stocks
    private var currentPage = 0
    private val pageSize = 11

    private val searchText = MutableStateFlow<String?>(null)
    private var job: Job? = null

    private val onFailure: (Throwable) -> Unit = {
        viewModelScope.launch {
            if (it !is CancellationException)
                sideEffect.send(ShowErrorNotification("Something went wrong."))
            state.updateIfType { c: Content -> c.copy(isPageLoading = false) }
        }
    }

    init {
        requestInitialStocks()
        searchText
            .debounce(500)
            .distinctUntilChanged()
            .filterNotNull()
            .onEach { performSearch(it) }
            .launchIn(viewModelScope)
    }

    internal fun handleInteraction(action: SearchInteraction) = when(action) {
        Retry -> requestInitialStocks()
        LoadNextPage -> requestMoreStocks()
        is SearchValueChanged -> onSearchChanged(action.text)
        is ItemClicked -> onItemClicked(action.index)
    }

    private fun requestInitialStocks() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStocks(currentPage, pageSize).fold(
                onSuccess = {
                    currentPage += pageSize
                    currentStocks = it
                    it.toContent(isEndReached())
                },
                onFailure = { Error }
            )

            state.emit(newState)
        }
    }

    private fun requestMoreStocks() {
        job?.cancel()
        job = viewModelScope.launch {
            state.updateIfType { c: Content -> c.copy(isPageLoading = true) }

            repository.getStocks(currentPage, pageSize, searchText.value?.uppercase()).fold(
                onSuccess = {
                    currentPage += pageSize
                    currentStocks = currentStocks.copy(items = currentStocks.items.plus(it.items))
                    state.updateIfType { c: Content ->
                        c.copy(
                            items = currentStocks.items.toPresentationEntity(),
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
        state.updateIfType { c: Content -> c.copy(search = text) }
        searchText.value = text
    }

    private suspend fun performSearch(text: String) {
        job?.cancelAndJoin()
        job = viewModelScope.launch {
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
                    currentStocks = it
                    state.updateIfType { c: Content ->
                        c.copy(
                            isSearchLoading = false,
                            items = currentStocks.items.toPresentationEntity(),
                            isEndReached = isEndReached(),
                        )
                    }
                },
                onFailure = onFailure,
            )
        }
    }

    private fun isEndReached() = currentPage >= currentStocks.pages

    private fun onItemClicked(index: Int) {
        val symbol = currentStocks.items[index].symbol
        sideEffect.trySend(NavigateToDetail(symbol))
    }
}
