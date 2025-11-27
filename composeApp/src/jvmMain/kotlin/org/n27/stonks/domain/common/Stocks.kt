package org.n27.stonks.domain.common

data class Stocks(
    val items: List<Stock>,
    val nextPage: Int? = null,
)
