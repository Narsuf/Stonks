package org.n27.stonks.presentation.home.entities

internal sealed class HomeEvent {

    data object CloseBottomSheet: HomeEvent()
    data class ShowBottomSheet(val index: Int) : HomeEvent()
}
