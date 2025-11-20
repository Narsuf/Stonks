package org.n27.stonks.data.search.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class SearchStockRaw(
    val symbol: String,
    val logoUrl: String?,
    val companyName: String,
    val price: Double?,
    val currency: String?,
)
