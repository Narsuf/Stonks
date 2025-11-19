package org.n27.stonks.data

import org.n27.stonks.data.mapping.toDomainEntity
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.domain.Stocks

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStocks(from: Int, size: Int, symbol: String?): Result<Stocks> = runCatching {
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
