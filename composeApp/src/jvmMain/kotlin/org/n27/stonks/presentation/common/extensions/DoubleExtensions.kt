package org.n27.stonks.presentation.common.extensions

import java.math.RoundingMode

internal fun Double.toFormattedBigDecimal() = toString()
    .toBigDecimal()
    .setScale(2, RoundingMode.HALF_UP)

internal fun Double.toFormattedString() = toFormattedBigDecimal().toPlainString()

internal fun Double.toFormattedPercentage() = "${toFormattedString()} %"
