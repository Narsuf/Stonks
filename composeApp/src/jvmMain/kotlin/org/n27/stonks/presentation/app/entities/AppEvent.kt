package org.n27.stonks.presentation.app.entities

import org.jetbrains.compose.resources.StringResource

internal sealed class AppEvent {

    data class ShowErrorNotification(val title: StringResource) : AppEvent()
}

