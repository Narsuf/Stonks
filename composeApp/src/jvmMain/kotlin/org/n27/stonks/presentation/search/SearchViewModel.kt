package org.n27.stonks.presentation.search

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.search.Search
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event.*
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch.Origin
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.detail.DetailParams
import org.n27.stonks.presentation.search.entities.SearchInteraction
import org.n27.stonks.presentation.search.entities.SearchInteraction.*
import org.n27.stonks.presentation.search.entities.SearchState
import org.n27.stonks.presentation.search.entities.SearchState.*
import org.n27.stonks.presentation.search.mapping.toContent
import org.n27.stonks.presentation.search.mapping.toPresentationEntity

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val origin: Origin,
    private val eventBus: EventBus,
    private val repository: Repository,
) : ViewModel() {
    private val state = MutableStateFlow<SearchState>(Idle)
    internal val viewState = state.asStateFlow()

    private lateinit var currentSearch: Search
    private var currentPage = 0
    private val pageSize = 11
    private val filterWatchlist = origin == Origin.WATCHLIST

    private val searchText = MutableStateFlow<String?>(null)
    private var job: Job? = null

    init {
        requestInitialStocks()
        searchText
            .debounce(500)
            .distinctUntilChanged()
            .filterNotNull()
            .onEach(::performSearch)
            .launchIn(viewModelScope)
    }

    internal fun handleInteraction(action: SearchInteraction) = when(action) {
        Retry -> requestInitialStocks()
        LoadNextPage -> requestMoreStocks()
        BackClicked -> onBackClicked()
        is SearchValueChanged -> onSearchChanged(action.text)
        is ItemClicked -> onItemClicked(action.index)
    }

    private fun requestInitialStocks() {
        viewModelScope.launch {
            state.emit(Loading)

            val newState = repository.getStocks(
                from = currentPage,
                size = pageSize,
                filterWatchlist = filterWatchlist,
            ).onSuccess {
                currentPage += pageSize
                currentSearch = it
            }.fold(
                onSuccess = { it.toContent(isEndReached()) },
                onFailure = { Error },
            )

            state.emit(newState)
        }
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
            state.updateIfType { c: Content ->
                repository.getStocks(
                    from = currentPage,
                    size = pageSize,
                    symbol = text.uppercase(),
                    filterWatchlist = filterWatchlist,
                ).onSuccess {
                    currentPage += pageSize
                    currentSearch = it
                    if (currentSearch.items.isEmpty())
                        eventBus.emit(ShowErrorNotification("No assets found."))
                }.onFailure {
                    it.showErrorNotification()
                }.fold(
                    onSuccess = {
                        c.copy(
                            isSearchLoading = false,
                            items = currentSearch.items.toPresentationEntity(),
                            isEndReached = isEndReached(),
                        )
                    },
                    onFailure = {
                        c.copy(
                            isSearchLoading = false,
                            isPageLoading = false,
                        )
                    },
                )
            }
        }
    }

    private fun requestMoreStocks() {
        job = viewModelScope.launch {
            state.updateIfType { c: Content -> c.copy(isPageLoading = true) }
            state.updateIfType { c: Content ->
                repository.getStocks(
                    from = currentPage,
                    size = pageSize,
                    symbol = searchText.value?.uppercase(),
                    filterWatchlist = filterWatchlist
                ).onSuccess {
                    currentPage += pageSize
                    currentSearch = currentSearch.copy(items = currentSearch.items.plus(it.items))
                }.onFailure {
                    it.showErrorNotification()
                }.fold(
                    onSuccess = {
                        c.copy(
                            items = currentSearch.items.toPresentationEntity(),
                            isPageLoading = false,
                            isEndReached = isEndReached(),
                        )
                    },
                    onFailure = { c.copy(isPageLoading = false) },
                )
            }
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            job?.cancel()
            eventBus.emit(GoBack())
        }
    }

    private fun onSearchChanged(text: String) {
        state.updateIfType { c: Content -> c.copy(search = text) }
        searchText.value = text
    }

    private fun onItemClicked(index: Int) {
        viewModelScope.launch {
            val symbol = currentSearch.items[index].symbol
            eventBus.emit(
                when (origin) {
                    Origin.HOME -> NavigateToDetail(DetailParams(symbol))
                    Origin.WATCHLIST -> GoBack(symbol)
                }
            )
        }
    }

    private fun isEndReached() = currentPage >= currentSearch.pages

    private suspend fun Throwable.showErrorNotification() {
        if (this !is CancellationException)
            eventBus.emit(ShowErrorNotification("Something went wrong."))
    }
}
