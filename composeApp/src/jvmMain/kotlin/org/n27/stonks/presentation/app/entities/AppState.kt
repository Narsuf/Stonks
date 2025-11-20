package org.n27.stonks.presentation.app.entities

sealed class AppState {

    data object Home: AppState()
    data object Detail: AppState()
}
