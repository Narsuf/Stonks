package org.n27.stonks.presentation.search.mapping

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.domain.Stock
import org.n27.stonks.domain.domain.Stocks
import org.n27.stonks.presentation.search.entities.SearchState
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency

internal fun Stocks.toContent() = SearchState.Content(
    search = "Search ticker...",
    items = items.map { it.toPresentationEntity() }.toPersistentList(),
)

private fun Stock.toPresentationEntity() = SearchState.Content.Item(
    iconUrl = logoUrl ?: "",
    name = companyName.truncateAfterDoubleSpace(),
    symbol = symbol,
    price = price?.toString()?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)?.toPrice(currency),
)

private fun String.truncateAfterDoubleSpace(): String {
    val index = Regex(" {3,}").find(this)?.range?.first
    return if (index != null)
        this.substring(0, index)
    else
        this
}

private fun BigDecimal.toPrice(currency: String?): String? = currency?.let {
    val format = NumberFormat.getCurrencyInstance()
        .apply { this.currency = Currency.getInstance(currency) }
    return format.format(this)
}
