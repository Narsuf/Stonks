package org.n27.stonks.presentation.common.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal fun Long.toDateString(
    pattern: String = "d. MMM HH:mm",
    zone: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return Instant.ofEpochMilli(this)
        .atZone(zone)
        .format(formatter)
}
