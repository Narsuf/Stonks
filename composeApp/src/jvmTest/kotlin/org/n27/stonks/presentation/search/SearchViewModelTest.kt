package org.n27.stonks.presentation.search

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.n27.stonks.SYMBOL
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.broadcast.Event.*
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.search.entities.SearchInteraction.*
import org.n27.stonks.presentation.search.entities.SearchState.*
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.domain.getStocks
import org.n27.stonks.test_data.presentation.getSearchContent
import org.n27.stonks.test_data.presentation.getSearchContentItem
import org.n27.stonks.utils.test
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.error_generic
import stonks.composeapp.generated.resources.error_no_assets
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val repository: Repository = mock()
    private val eventBus = EventBus()

    @Before
    fun init() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())
        `when`(repository.getStocks(filterWatchlist = false))
            .thenReturn(success(getStocks()))
    }

    @Test
    fun `should emit loading and content when init`() = runTest {
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))

        runCurrent()

        observer.assertValues(Idle, Loading, getSearchContent())
        observer.close()
    }

    @Test
    fun `should emit loading and content when retry`() = runTest {
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.handleInteraction(Retry)
        runCurrent()

        observer.assertValues(Loading, getSearchContent())
        observer.close()
    }

    @Test
    fun `should emit go back when back button clicked`() = runTest {
        val viewModel = getViewModel()
        val observer = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))

        viewModel.handleInteraction(BackClicked)
        runCurrent()

        observer.assertValues(GoBack())
        observer.close()
    }

    @Test
    fun `should emit error when get stocks fails`() = runTest {
        `when`(repository.getStocks(
            filterWatchlist = false,
        )).thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))

        runCurrent()

        observer.assertValues(Idle, Loading, Error)
        observer.close()
    }

    @Test
    fun `should emit page loading and content when load next page`() = runTest {
        `when`(repository.getStocks(
            filterWatchlist = false,
            from = 2,
            symbol = null,
        )).thenReturn(success(getStocks()))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.handleInteraction(LoadNextPage)
        runCurrent()

        observer.assertValues(
            getSearchContent().copy(isPageLoading = true),
            getSearchContent().copy(
                isPageLoading = false,
                items = persistentListOf(
                    getSearchContentItem(),
                    getSearchContentItem(),
                ),
            )
        )
        observer.close()
    }

    @Test
    fun `should emit error when load next page fails`() = runTest {
        `when`(repository.getStocks(
            filterWatchlist = false,
            from = 2,
            symbol = null,
        )).thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(LoadNextPage)
        runCurrent()

        stateObserver.assertValues(
            getSearchContent().copy(isPageLoading = true),
            getSearchContent().copy(isPageLoading = false)
        )
        eventObserver.assertValues(ShowErrorNotification(Res.string.error_generic))
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should emit search loading and content when search value changed`() = runTest {
        `when`(repository.getStocks(
            filterWatchlist = false,
            symbol = "TEST",
        )).thenReturn(success(getStocks(items = listOf(getStock(symbol = "TEST")))))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.handleInteraction(SearchValueChanged("test"))
        runCurrent()
        advanceTimeBy(500)
        runCurrent()

        observer.assertValues(
            getSearchContent().copy(search = "test"),
            getSearchContent().copy(
                search = "test",
                isSearchLoading = true,
                items = persistentListOf()
            ),
            getSearchContent().copy(
                search = "test",
                isSearchLoading = false,
                items = persistentListOf(getSearchContentItem(symbol = "TEST"))
            )
        )
        observer.close()
    }

    @Test
    fun `should emit error notification when search returns empty list`() = runTest {
        `when`(repository.getStocks(
            filterWatchlist = false,
            symbol = "TEST",
        )).thenReturn(success(getStocks(items = listOf())))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(SearchValueChanged("test"))
        runCurrent()
        advanceTimeBy(500)
        runCurrent()

        stateObserver.assertValues(
            getSearchContent().copy(search = "test"),
            getSearchContent().copy(
                search = "test",
                isSearchLoading = true,
                items = persistentListOf()
            ),
            getSearchContent().copy(
                search = "test",
                isSearchLoading = false,
                items = persistentListOf()
            )
        )
        eventObserver.assertValues(ShowErrorNotification(Res.string.error_no_assets))
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should emit error notification when search returns error`() = runTest {
        `when`(repository.getStocks(
            filterWatchlist = false,
            symbol = "TEST",
        )).thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(SearchValueChanged("test"))
        runCurrent()
        advanceTimeBy(500)
        runCurrent()

        stateObserver.assertValues(
            getSearchContent().copy(search = "test"),
            getSearchContent().copy(
                search = "test",
                isSearchLoading = true,
                items = persistentListOf()
            ),
            getSearchContent().copy(
                search = "test",
                isSearchLoading = false,
                items = persistentListOf(),
                isEndReached = true,
            )
        )
        eventObserver.assertValues(ShowErrorNotification(Res.string.error_generic))
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should emit navigate to detail when item clicked`() = runTest {
        val viewModel = getViewModel()
        val observer = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()

        viewModel.handleInteraction(ItemClicked(0))
        runCurrent()

        observer.assertValues(NavigateToDetail("AAPL"))
        observer.close()
    }

    @Test
    fun `should emit go back when item clicked`() = runTest {
        `when`(repository.getStocks(filterWatchlist = true))
            .thenReturn(success(getStocks()))
        val viewModel = getViewModel(origin = NavigateToSearch.Watchlist)
        val observer = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()

        viewModel.handleInteraction(ItemClicked(0))
        runCurrent()

        observer.assertValues(GoBack(mapOf(SYMBOL to "AAPL")))
        observer.close()
    }

    private fun TestScope.getViewModel(
        origin: NavigateToSearch = NavigateToSearch.All,
    ) = SearchViewModel(
        mode = origin,
        eventBus = eventBus,
        repository = repository,
        dispatcher = StandardTestDispatcher(testScheduler),
    )
}
