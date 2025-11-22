package org.n27.stonks.presentation.common.extensions

import androidx.compose.ui.Modifier

fun <T> Modifier.ifNotNull(value: T?, block: Modifier.(T) -> Modifier): Modifier {
    return if (value != null) this.block(value) else this
}
