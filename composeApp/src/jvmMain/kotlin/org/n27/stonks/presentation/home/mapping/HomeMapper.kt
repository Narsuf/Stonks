package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.domain.home.Home
import org.n27.stonks.domain.home.Home.Stock
import org.n27.stonks.presentation.common.extensions.toFormattedBigDecimal
import org.n27.stonks.presentation.common.extensions.toPrice
import org.n27.stonks.presentation.common.extensions.truncateAfterDoubleSpace
import org.n27.stonks.presentation.home.entities.HomeState.Content

internal fun Home.toContent() = Content(
    watchlist = items.map { it.toPresentationEntity() }.toPersistentList()
)

private fun Stock.toPresentationEntity() = Content.Item(
    iconUrl = logoUrl ?: "",
    name = companyName.truncateAfterDoubleSpace(),
    symbol = symbol,
    price = price?.toFormattedBigDecimal()?.toPrice(currency),
    estimatedEpsGrowth = null,
)
