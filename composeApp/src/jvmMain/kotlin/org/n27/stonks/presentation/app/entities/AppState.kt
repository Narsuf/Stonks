package org.n27.stonks.presentation.app.entities

sealed class AppState {

    data object Search: AppState()
    data object Detail: AppState()
}
