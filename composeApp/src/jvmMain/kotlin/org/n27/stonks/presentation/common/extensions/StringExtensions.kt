package org.n27.stonks.presentation.common.extensions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

internal fun String.truncateAfterDoubleSpace(): String {
    val index = Regex(" {3,}").find(this)?.range?.first
    return if (index != null)
        this.substring(0, index)
    else
        this
}

internal fun String.toFormattedDate(): String = runCatching {
    val date = LocalDate.parse(this)
    val pattern = if (date.dayOfMonth == 1) "MMM yyyy" else "dd MMM yyyy"
    date.format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH))
}.getOrElse {
    runCatching {
        YearMonth.parse(this).format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH))
    }.getOrDefault(this)
}

internal fun String.toImageBitmap(): ImageBitmap? = runCatching {
    val decodedBytes = Base64.getDecoder().decode(this)
    Image.makeFromEncoded(decodedBytes).toComposeImageBitmap()
}.getOrNull()
