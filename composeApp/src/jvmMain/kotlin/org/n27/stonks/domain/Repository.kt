package org.n27.stonks.domain

import org.n27.stonks.domain.domain.Stocks

interface Repository {

    suspend fun getStocks(from: Int, size: Int, symbol: String? = null): Result<Stocks>
}
