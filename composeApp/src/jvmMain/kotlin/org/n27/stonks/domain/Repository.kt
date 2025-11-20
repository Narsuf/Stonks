package org.n27.stonks.domain

import org.n27.stonks.domain.common.Stock
import org.n27.stonks.domain.search.Search

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
    suspend fun getStocks(from: Int, size: Int, symbol: String? = null): Result<Search>
}
