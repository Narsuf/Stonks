package org.n27.stonks.presentation.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.mock
import org.n27.stonks.presentation.app.entities.AppEvent
import org.n27.stonks.presentation.app.entities.AppState.*
import org.n27.stonks.presentation.common.broadcast.Event.*
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch.Origin
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.detail.DetailViewModel
import org.n27.stonks.presentation.home.HomeViewModel
import org.n27.stonks.presentation.search.SearchViewModel
import org.n27.stonks.utils.test
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.error_generic

@ExperimentalCoroutinesApi
class AppViewModelTest {

    private val eventBus = EventBus()
    private val mockHomeViewModel: HomeViewModel = mock()
    private val mockSearchViewModel: SearchViewModel = mock()
    private val mockDetailViewModel: DetailViewModel = mock()

    @Before
    fun init() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())
        stopKoin()
        startKoin {
            modules(
                module {
                    single { mockHomeViewModel }
                    single { (_: Origin) -> mockSearchViewModel }
                    single { (_: String) -> mockDetailViewModel }
                }
            )
        }
    }

    @Test
    fun `should start with Home state`() = runTest {
        val viewModel = getViewModel()
        val observer = viewModel.viewState.test(this + UnconfinedTestDispatcher(testScheduler))

        runCurrent()

        assert(viewModel.viewState.value is Home)
        observer.close()
    }

    @Test
    fun `should push Search state when NavigateToSearch event is emitted`() = runTest {
        val viewModel = getViewModel()
        runCurrent()

        eventBus.emit(NavigateToSearch())
        runCurrent()

        assert(viewModel.viewState.value is Search)
    }

    @Test
    fun `should push Search state with Watchlist origin when NavigateToSearch with origin is emitted`() = runTest {
        val viewModel = getViewModel()
        runCurrent()

        eventBus.emit(NavigateToSearch(Origin.WATCHLIST))
        runCurrent()

        assert(viewModel.viewState.value is Search)
    }

    @Test
    fun `should push Detail state when NavigateToDetail event is emitted`() = runTest {
        val viewModel = getViewModel()
        runCurrent()

        eventBus.emit(NavigateToDetail("AAPL"))
        runCurrent()

        assert(viewModel.viewState.value is Detail)
    }

    @Test
    fun `should emit ShowErrorNotification event when ShowErrorNotification is received`() = runTest {
        val viewModel = getViewModel()
        val eventObserver = viewModel.viewEvent.test(this + UnconfinedTestDispatcher(testScheduler))
        runCurrent()

        eventBus.emit(ShowErrorNotification(Res.string.error_generic))
        runCurrent()

        eventObserver.assertValues(AppEvent.ShowErrorNotification(Res.string.error_generic))
        eventObserver.close()
    }

    @Test
    fun `should pop back through multiple states correctly`() = runTest {
        val viewModel = getViewModel()
        runCurrent()

        // Navigate: Home -> Search -> Detail
        eventBus.emit(NavigateToSearch())
        runCurrent()
        eventBus.emit(NavigateToDetail("AAPL"))
        runCurrent()
        assert(viewModel.viewState.value is Detail)

        // Pop: Detail -> Search
        eventBus.emit(GoBack())
        runCurrent()
        assert(viewModel.viewState.value is Search)

        // Pop: Search -> Home
        eventBus.emit(GoBack())
        runCurrent()
        assert(viewModel.viewState.value is Home)
    }

    private fun TestScope.getViewModel() = AppViewModel(
        eventBus = eventBus,
        dispatcher = StandardTestDispatcher(testScheduler),
    )
}

