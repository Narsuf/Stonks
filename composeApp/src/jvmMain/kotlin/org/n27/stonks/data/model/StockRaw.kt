package org.n27.stonks.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class StockRaw(
    val symbol: String,
    val logoUrl: String,
    val companyName: String,
    val price: Double,
    val eps: Double,
    val trailingPe: Double,
    val dividendYield: Double,
    val earningsQuarterlyGrowth: Double,
    val currency: String,
    val intrinsicValue: Double
)