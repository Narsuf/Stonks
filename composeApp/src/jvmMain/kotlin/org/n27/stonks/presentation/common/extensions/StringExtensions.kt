package org.n27.stonks.presentation.common.extensions

internal fun String.truncateAfterDoubleSpace(): String {
    val index = Regex(" {3,}").find(this)?.range?.first
    return if (index != null)
        this.substring(0, index)
    else
        this
}
