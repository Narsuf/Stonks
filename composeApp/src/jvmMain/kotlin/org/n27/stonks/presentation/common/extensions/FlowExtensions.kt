package org.n27.stonks.presentation.common.extensions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

inline fun <T, reified Type : T> MutableStateFlow<T>.updateIfType(function: (Type) -> T) {
    update { it.runIfType<_, Type> { function(this) } }
}
