package org.n27.stonks

import org.koin.dsl.module
import org.n27.stonks.presentation.search.SearchViewModel

val appModule = module {
    factory { SearchViewModel() }
}