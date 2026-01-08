package org.n27.stonks.presentation.common.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.util.*

internal fun String.truncateAfterDoubleSpace(): String {
    val index = Regex(" {3,}").find(this)?.range?.first
    return if (index != null)
        this.substring(0, index)
    else
        this
}

internal fun String.toImageBitmap(): ImageBitmap? = runCatching {
    val decodedBytes = Base64.getDecoder().decode(this)
    Image.makeFromEncoded(decodedBytes).toComposeImageBitmap()
}.getOrNull()
