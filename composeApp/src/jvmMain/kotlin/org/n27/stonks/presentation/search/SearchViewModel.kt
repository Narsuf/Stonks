package org.n27.stonks.presentation.search

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.n27.stonks.presentation.common.ViewModel
import org.n27.stonks.presentation.search.entities.SearchState
import org.n27.stonks.presentation.search.entities.SearchState.Content
import org.n27.stonks.presentation.search.entities.SearchState.Idle
import org.n27.stonks.presentation.search.entities.SearchState.Loading
import java.math.BigDecimal
import java.util.Currency

class SearchViewModel() : ViewModel() {

    private val state = MutableStateFlow<SearchState>(Idle)
    val viewState = state.asStateFlow()

    init { onScreenOpened() }

    fun onScreenOpened() {
        viewModelScope.launch {
            state.emit(Loading)
            state.emit(
                Content(
                    search = "Search ticker...",
                    items = persistentListOf(
                        Content.Item
                            (
                            iconUrl = "https://logo.clearbit.com/microsoft.com",
                            name = "Microsoft Corporation",
                            symbol = "MSFT",
                            price = BigDecimal("127.45"),
                            currency = Currency.getInstance("USD"),
                        ),
                        Content.Item(
                            iconUrl = "https://logo.clearbit.com/apple.com",
                            name = "Apple Inc.",
                            symbol = "AAPL",
                            price = BigDecimal("267.77"),
                            currency = Currency.getInstance("USD"),
                        )
                    )
                )
            )
        }
    }
}