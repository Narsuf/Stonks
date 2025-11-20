package org.n27.stonks.presentation.app.entities

internal sealed class AppEvent {

    data class ShowErrorNotification(val title: String) : AppEvent()
}

