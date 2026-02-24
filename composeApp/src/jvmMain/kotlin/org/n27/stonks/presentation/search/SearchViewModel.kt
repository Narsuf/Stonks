package org.n27.stonks.presentation.search

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.n27.stonks.SYMBOL
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.models.Stocks
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.common.broadcast.Event.*
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.common.extensions.updateIfType
import org.n27.stonks.presentation.search.entities.SearchInteraction
import org.n27.stonks.presentation.search.entities.SearchInteraction.*
import org.n27.stonks.presentation.search.entities.SearchState
import org.n27.stonks.presentation.search.entities.SearchState.*
import org.n27.stonks.presentation.search.mapping.toContent
import org.n27.stonks.presentation.search.mapping.toPresentationEntity
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.error_generic
import stonks.composeapp.generated.resources.error_no_assets

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val mode: NavigateToSearch,
    private val eventBus: EventBus,
    private val repository: Repository,
    dispatcher: CoroutineDispatcher,
) : ViewModel(dispatcher) {
    private val state = MutableStateFlow<SearchState>(Idle)
    internal val viewState = state.asStateFlow()

    private lateinit var currentStocks: Stocks
    private val filterWatchlist = mode is NavigateToSearch.Watchlist

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
                filterWatchlist = filterWatchlist,
            ).onSuccess {
                currentStocks = it
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
            state.updateIfType { c: Content ->
                repository.getStocks(
                    symbol = text.uppercase(),
                    filterWatchlist = filterWatchlist,
                ).onSuccess {
                    currentStocks = it
                    if (currentStocks.items.isEmpty())
                        eventBus.emit(ShowErrorNotification(Res.string.error_no_assets))
                }.onFailure {
                    it.showErrorNotification()
                }.fold(
                    onSuccess = {
                        c.copy(
                            isSearchLoading = false,
                            items = currentStocks.items.toPresentationEntity(),
                            isEndReached = isEndReached(),
                        )
                    },
                    onFailure = {
                        c.copy(
                            isSearchLoading = false,
                            isPageLoading = false,
                            isEndReached = true,
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
                    from = currentStocks.nextPage,
                    symbol = searchText.value?.uppercase(),
                    filterWatchlist = filterWatchlist
                ).onSuccess {
                    currentStocks = currentStocks.copy(
                        items = currentStocks.items.plus(it.items),
                        nextPage = it.nextPage,
                    )
                }.onFailure {
                    it.showErrorNotification()
                }.fold(
                    onSuccess = {
                        c.copy(
                            items = currentStocks.items.toPresentationEntity(),
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
            val symbol = currentStocks.items[index].symbol
            eventBus.emit(
                when (mode) {
                    is NavigateToSearch.All -> NavigateToDetail(symbol)
                    is NavigateToSearch.Watchlist -> GoBack(mapOf(SYMBOL to symbol))
                }
            )
        }
    }

    private fun isEndReached() = currentStocks.nextPage == null

    private suspend fun Throwable.showErrorNotification() {
        if (this !is CancellationException)
            eventBus.emit(ShowErrorNotification(Res.string.error_generic))
    }
}
