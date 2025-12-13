package org.n27.stonks.data.watchlist

import kotlinx.serialization.Serializable

@Serializable
data class StockInfo(
    val symbol: String,
    val expectedEpsGrowth: Double? = null,
    val valuationFloor: Double? = null,
)
