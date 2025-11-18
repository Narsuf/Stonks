package org.n27.stonks.data

import org.n27.stonks.data.mapping.toDomainEntity
import org.n27.stonks.domain.Repository
import org.n27.stonks.domain.domain.Stocks

class RepositoryImpl(private val api: Api) : Repository {

    override suspend fun getStocks(symbols: List<String>): Result<Stocks> = runCatching {
        api.getStocks(symbols).toDomainEntity()
    }
}