package org.n27.stonks.di

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.n27.stonks.data.*
import org.n27.stonks.data.eurostat.EurostatApi
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.app.AppViewModel
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.detail.DetailViewModel
import org.n27.stonks.presentation.home.HomeViewModel
import org.n27.stonks.presentation.search.SearchViewModel

val appModule = module {

    single {
        HttpClient {
            install(HttpTimeout) {
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 20_000
                requestTimeoutMillis = 20_000
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
        }
    }

    single { Api(System.getProperty("STONKS_URL") ?: System.getenv("STONKS_URL"), get()) }
    single { FredApi(get()) }
    single { EurostatApi(get()) }
    single { MacroIndicatorsCache() }
    single { MacroIndicatorsStore(get(), get(), get()) }
    single<Repository> { RepositoryImpl(get()) }

    single { EventBus() }

    factory { AppViewModel(get(), get(), Dispatchers.Default) }
    factory { HomeViewModel(get(), get(), Dispatchers.Default) }
    factory { (origin: NavigateToSearch) -> SearchViewModel(origin, get(), get(), Dispatchers.Default) }
    factory { (symbol: String) -> DetailViewModel(symbol, get(), get(), get(), Dispatchers.Default) }
}
