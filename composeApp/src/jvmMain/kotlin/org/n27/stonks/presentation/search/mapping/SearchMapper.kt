package org.n27.stonks.presentation.search.mapping

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.search.Search
import org.n27.stonks.presentation.search.entities.SearchState.Content
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

internal fun Search.toContent(isEndReached: Boolean) = Content(
    search = "",
    isSearchLoading = false,
    items = items.toPresentationEntity(),
    isPageLoading = false,
    isEndReached = isEndReached,
)

internal fun List<Search.Stock>.toPresentationEntity(): ImmutableList<Content.Item> = map { it.toPresentationEntity() }
    .toPersistentList()

private fun Search.Stock.toPresentationEntity() = Content.Item(
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

private fun BigDecimal.toPrice(currency: String?): String? {
    val safeCurrency = runCatching {
        currency?.uppercase()?.let { Currency.getInstance(it) }
    }.getOrNull()

    val format = NumberFormat.getCurrencyInstance()
    safeCurrency?.let { format.currency = it }

    return format.format(this)
}
