package org.n27.stonks.presentation.home

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
import org.n27.stonks.presentation.home.entities.HomeEvent
import org.n27.stonks.presentation.home.entities.HomeInteraction.*
import org.n27.stonks.presentation.home.entities.HomeState.*
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.domain.getStocks
import org.n27.stonks.test_data.presentation.getHomeBottomSheet
import org.n27.stonks.test_data.presentation.getHomeContent
import org.n27.stonks.test_data.presentation.getHomeItem
import org.n27.stonks.utils.test
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.error_generic
import java.math.BigDecimal
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val repository: Repository = mock()
    private val eventBus = EventBus()

    @Before
    fun init() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())
        `when`(repository.getWatchlist())
            .thenReturn(success(getStocks()))
    }

    @Test
    fun `should emit loading and content when init`() = runTest {
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))

        runCurrent()

        observer.assertValues(Idle, Loading, getHomeContent())
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

        observer.assertValues(Loading, getHomeContent())
        observer.close()
    }

    @Test
    fun `should emit error when get watchlist fails`() = runTest {
        `when`(repository.getWatchlist()).thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))

        runCurrent()

        observer.assertValues(Idle, Loading, Error)
        observer.close()
    }

    @Test
    fun `should emit navigate to search when search clicked`() = runTest {
        val viewModel = getViewModel()
        val observer = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()

        viewModel.handleInteraction(SearchClicked)
        runCurrent()

        observer.assertValues(NavigateToSearch.All)
        observer.close()
    }

    @Test
    fun `should emit navigate to search with origin watchlist when add clicked`() = runTest {
        val viewModel = getViewModel()
        val observer = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()

        viewModel.handleInteraction(AddClicked)
        runCurrent()

        observer.assertValues(NavigateToSearch.Watchlist)
        observer.close()
    }

    @Test
    fun `should emit page loading and content when load next page`() = runTest {
        `when`(repository.getWatchlist(from = 2))
            .thenReturn(success(getStocks()))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.handleInteraction(LoadNextPage)
        runCurrent()

        observer.assertValues(
            getHomeContent().copy(isPageLoading = true),
            getHomeContent().copy(
                isPageLoading = false,
                watchlist = persistentListOf(
                    getHomeItem(),
                    getHomeItem(),
                ),
            )
        )
        observer.close()
    }

    @Test
    fun `should emit error when load next page fails`() = runTest {
        `when`(repository.getWatchlist(from = 2))
            .thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(LoadNextPage)
        runCurrent()

        stateObserver.assertValues(getHomeContent().copy(isPageLoading = true))
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
    fun `should remove item from watchlist when remove item clicked`() = runTest {
        `when`(repository.removeFromWatchlist("AAPL"))
            .thenReturn(success(Unit))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.handleInteraction(RemoveItemClicked(0))
        runCurrent()

        observer.assertValues(getHomeContent().copy(watchlist = persistentListOf()))
        observer.close()
    }

    @Test
    fun `should emit error notification when remove item fails`() = runTest {
        `when`(repository.removeFromWatchlist("AAPL"))
            .thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(RemoveItemClicked(0))
        runCurrent()

        stateObserver.assertValues()
        eventObserver.assertValues(ShowErrorNotification(Res.string.error_generic))
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should emit show bottom sheet event and update state when edit item clicked`() = runTest {
        `when`(repository.getWatchlist()).thenReturn(
            success(getStocks(
                items = listOf(
                    getStock(
                        symbol = "AAPL",
                        expectedEpsGrowth = 7.72,
                        valuationFloor = 12.5
                    )
                )
            ))
        )
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = viewModel.viewEvent.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(EditItemClicked(0))
        runCurrent()

        stateObserver.assertValues(
            getHomeContent().copy(
                bottomSheet = getHomeBottomSheet(
                    epsGrowthInput = BigDecimal("7.72"),
                    valuationFloorInput = BigDecimal("12.50")
                )
            )
        )
        eventObserver.assertValues(HomeEvent.ShowBottomSheet(0))
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should update eps growth input when eps growth value changed`() = runTest {
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.handleInteraction(EpsGrowthValueChanged(BigDecimal("10.5")))
        runCurrent()

        observer.assertValues(
            getHomeContent().copy(
                bottomSheet = getHomeBottomSheet(epsGrowthInput = BigDecimal("10.5"))
            )
        )
        observer.close()
    }

    @Test
    fun `should update valuation floor input when valuation floor value changed`() = runTest {
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.handleInteraction(ValuationFloorValueChanged(BigDecimal("15.0")))
        runCurrent()

        observer.assertValues(
            getHomeContent().copy(
                bottomSheet = getHomeBottomSheet(valuationFloorInput = BigDecimal("15.0"))
            )
        )
        observer.close()
    }

    @Test
    fun `should update watchlist item and close bottom sheet when values updated`() = runTest {
        `when`(repository.editWatchlistItem("AAPL", 10.5, 15.0))
            .thenReturn(success(Unit))
        `when`(repository.getWatchlist()).thenReturn(success(getStocks()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = viewModel.viewEvent.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(ValuesUpdated(0, BigDecimal("10.5"), BigDecimal("15.0")))
        runCurrent()

        eventObserver.assertValues(HomeEvent.CloseBottomSheet)
        stateObserver.assertValues(Loading, getHomeContent())
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should update watchlist item with null valuation floor when zero provided`() = runTest {
        `when`(repository.editWatchlistItem("AAPL", 10.5, null))
            .thenReturn(success(Unit))
        `when`(repository.getWatchlist()).thenReturn(success(getStocks()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = viewModel.viewEvent.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(ValuesUpdated(0, BigDecimal("10.5"), BigDecimal.ZERO))
        runCurrent()

        eventObserver.assertValues(HomeEvent.CloseBottomSheet)
        stateObserver.assertValues(Loading, getHomeContent())
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should emit error notification when edit watchlist item fails`() = runTest {
        `when`(repository.editWatchlistItem("AAPL", 10.5, 15.0))
            .thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(ValuesUpdated(0, BigDecimal("10.5"), BigDecimal("15.0")))
        runCurrent()

        eventObserver.assertValues(ShowErrorNotification(Res.string.error_generic))
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should add stock to watchlist when onResult called with stock symbol`() = runTest {
        `when`(repository.addToWatchlist("AAPL"))
            .thenReturn(success(Unit))
        `when`(repository.getWatchlist()).thenReturn(success(getStocks()))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        observer.reset()

        viewModel.onResult(mapOf(SYMBOL to "AAPL"))
        runCurrent()

        observer.assertValues(Loading, getHomeContent())
        observer.close()
    }

    @Test
    fun `should emit error notification when add to watchlist fails`() = runTest {
        `when`(repository.addToWatchlist("AAPL"))
            .thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.onResult(mapOf(SYMBOL to "AAPL"))
        runCurrent()

        eventObserver.assertValues(ShowErrorNotification(Res.string.error_generic))
        stateObserver.close()
        eventObserver.close()
    }

    private fun TestScope.getViewModel() = HomeViewModel(
        eventBus = eventBus,
        repository = repository,
        dispatcher = StandardTestDispatcher(testScheduler),
    )
}
