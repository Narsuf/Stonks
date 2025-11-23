package org.n27.stonks.presentation.home.entities

import java.math.BigDecimal

internal sealed class HomeInteraction {

    data object Retry : HomeInteraction()
    data object SearchClicked : HomeInteraction()
    data object AddClicked : HomeInteraction()
    data object LoadNextPage : HomeInteraction()
    data class ItemClicked(val index: Int) : HomeInteraction()
    data class RemoveItemClicked(val index: Int) : HomeInteraction()
    data class EditItemClicked(val index: Int) : HomeInteraction()
    data class ValueChanged(val value: BigDecimal) : HomeInteraction()
    data class ValueUpdated(val index: Int, val value: BigDecimal) : HomeInteraction()
}
