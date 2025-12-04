package org.n27.stonks.di

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.n27.stonks.data.Api
import org.n27.stonks.data.RepositoryImpl
import org.n27.stonks.domain.Repository
import org.n27.stonks.presentation.app.AppViewModel
import org.n27.stonks.presentation.common.broadcast.Event.NavigateToSearch
import org.n27.stonks.presentation.common.broadcast.EventBus
import org.n27.stonks.presentation.detail.DetailParams
import org.n27.stonks.presentation.detail.DetailViewModel
import org.n27.stonks.presentation.home.HomeViewModel
import org.n27.stonks.presentation.search.SearchViewModel

const val BASE_URL = "http://localhost:8000"

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

    single {
        Api(
            httpClient = get(),
            baseUrl = BASE_URL
        )
    }

    single { EventBus() }
    single<Repository> { RepositoryImpl(get()) }
    
    factory { AppViewModel(get()) }
    factory { HomeViewModel(get(), get()) }
    factory { (origin: NavigateToSearch.Origin) -> SearchViewModel(origin, get(), get()) }
    factory { (params: DetailParams) -> DetailViewModel(params, get(), get()) }
}
