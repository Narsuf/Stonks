package org.n27.stonks.data

import org.n27.stonks.data.common.mapping.toDomainEntity
import org.n27.stonks.data.search.mapping.toDomainEntity
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.common.Stock
import org.n27.stonks.domain.search.Search

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        api.getStock(symbol).toDomainEntity()
    }

    override suspend fun getStocks(from: Int, size: Int, symbol: String?): Result<Search> = runCatching {
        val params = symbol?.takeIf { it.isNotEmpty() }
            ?.let { getFilteredSymbols(it) }
            ?: JsonReader.getSymbols()

        val paginatedParams = params
            .drop(from)
            .take(size)
            .joinToString(separator = ",")

        api.getStocks(paginatedParams).toDomainEntity(params.size)
    }

    private suspend fun getFilteredSymbols(symbol: String): List<String> = JsonReader.getSymbols()
        .filter { it.contains(symbol) }
}
