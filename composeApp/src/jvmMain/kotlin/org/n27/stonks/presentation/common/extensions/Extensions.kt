package org.n27.stonks.presentation.common.extensions

inline fun <T, reified Type : T> T.runIfType(block: Type.() -> T): T {
    return if (this is Type) block(this) else this
}
