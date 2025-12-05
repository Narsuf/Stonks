package org.n27.stonks.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Stocks(
    val items: List<Stock>,
    val nextPage: Int? = null,
)
