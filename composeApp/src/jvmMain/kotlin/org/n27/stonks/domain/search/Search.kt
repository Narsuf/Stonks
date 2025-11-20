package org.n27.stonks.domain.search

data class Search(
    val pages: Int,
    val items: List<Stock>
) {

    data class Stock(
        val symbol: String,
        val logoUrl: String?,
        val companyName: String,
        val price: Double?,
        val currency: String?,
    )
}
