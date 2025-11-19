package org.n27.stonks.domain.domain

data class Stocks(
    val pages: Int,
    val items: List<Stock>
)
