package org.n27.stonks.presentation.detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.common.broadcast.Event.GoBack
import org.n27.stonks.presentation.common.broadcast.Event.WatchlistEvent
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.detail.entities.DetailInteraction.*
import org.n27.stonks.presentation.detail.entities.DetailState.*
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.presentation.getDetailContent
import org.n27.stonks.utils.test
import java.util.*
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    private val repository: Repository = mock()
    private val eventBus = EventBus()
    private val symbol = "AAPL"

    @Before
    fun init() = runTest {
        Locale.setDefault(Locale.US)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        Dispatchers.setMain(StandardTestDispatcher())
        `when`(repository.getStock(symbol)).thenReturn(success(getStock()))
    }

    @Test
    fun `should emit loading and content when init`() = runTest {
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))

        runCurrent()

        observer.assertValues(Idle, Loading, getDetailContent())
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

        observer.assertValues(Loading, getDetailContent())
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
    fun `should emit error when get stock fails`() = runTest {
        `when`(repository.getStock(symbol)).thenReturn(failure(Throwable()))
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))

        runCurrent()

        observer.assertValues(Idle, Loading, Error)
        observer.close()
    }

    @Test
    fun `should add to watchlist when not watchlisted`() = runTest {
        `when`(repository.getStock(symbol)).thenReturn(success(getStock(isWatchlisted = false)))
        `when`(repository.addToWatchlist(symbol)).thenReturn(success(Unit))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(ToggleWatchlist)
        runCurrent()

        verify(repository).addToWatchlist(symbol)
        eventObserver.assertValues(WatchlistEvent.AssetAdded)
        stateObserver.assertValues(getDetailContent(isWatchlisted = true))
        stateObserver.close()
        eventObserver.close()
    }

    @Test
    fun `should remove from watchlist when already watchlisted`() = runTest {
        `when`(repository.getStock(symbol)).thenReturn(success(getStock(isWatchlisted = true)))
        `when`(repository.removeFromWatchlist(symbol)).thenReturn(success(Unit))
        val viewModel = getViewModel()
        val stateObserver = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))
        val eventObserver = eventBus.events.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()
        stateObserver.reset()

        viewModel.handleInteraction(ToggleWatchlist)
        runCurrent()

        verify(repository).removeFromWatchlist(symbol)
        eventObserver.assertValues(WatchlistEvent.AssetRemoved)
        stateObserver.assertValues(getDetailContent(isWatchlisted = false))
        stateObserver.close()
        eventObserver.close()
    }

    private fun TestScope.getViewModel() = DetailViewModel(
        symbol = symbol,
        eventBus = eventBus,
        repository = repository,
        dispatcher = StandardTestDispatcher(testScheduler),
    )
}
