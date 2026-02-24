package org.n27.stonks.di

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.n27.stonks.data.Api
import org.n27.stonks.data.RepositoryImpl
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

    single { Api(System.getenv("STONKS_URL"), get()) }
    single<Repository> { RepositoryImpl(get()) }

    single { EventBus() }

    factory { AppViewModel(get(), Dispatchers.Default) }
    factory { HomeViewModel(get(), get(), Dispatchers.Default) }
    factory { (origin: NavigateToSearch) -> SearchViewModel(origin, get(), get(), Dispatchers.Default) }
    factory { (symbol: String) -> DetailViewModel(symbol, get(), get(), Dispatchers.Default) }
}
