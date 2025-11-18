package org.n27.stonks

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin

fun main() = application {

    startKoin { modules(appModule) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Stonks",
    ) {
        App()
    }
}