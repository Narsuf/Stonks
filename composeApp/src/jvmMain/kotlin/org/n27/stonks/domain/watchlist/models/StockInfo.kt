package org.n27.stonks.domain.watchlist.models

import kotlinx.serialization.Serializable

@Serializable
data class StockInfo(
    val symbol: String,
    val expectedEpsGrowth: Double? = null,
)
