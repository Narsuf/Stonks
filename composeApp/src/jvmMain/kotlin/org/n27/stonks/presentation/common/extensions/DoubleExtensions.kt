package org.n27.stonks.presentation.common.extensions

import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import java.math.RoundingMode
import kotlin.math.absoluteValue

internal fun Double.toFormattedBigDecimal() = toString()
    .toBigDecimal()
    .setScale(2, RoundingMode.HALF_UP)

internal fun Double.toPrice(currency: String?) = toFormattedBigDecimal().toPrice(currency)

internal fun Double.toFormattedString() = toFormattedBigDecimal().toPlainString()

internal fun Double.toFormattedPercentage() = "${toFormattedString()} %"

internal fun Double.toIntrinsicValue(expectedEpsGrowth: Double): Double = this * 12.5 * expectedEpsGrowth.toMultiplier()

internal fun Double.toMultiplier(): Double = 1 + this / 100

internal fun Double.getTargetPrice(
    intrinsicValue: Double?,
    currency: String?,
): DeltaTextEntity? {
    if (intrinsicValue == null) return null

    val priceDiff = intrinsicValue - this
    val percentage = if (this != 0.0)
        (priceDiff / this) * 100
    else
        0.0

    val state = when {
        priceDiff > 0 -> DeltaState.POSITIVE
        priceDiff < 0 -> DeltaState.NEGATIVE
        else -> DeltaState.NEUTRAL
    }

    return priceDiff.absoluteValue.toPrice(currency)?.let {
        DeltaTextEntity(
            value = it,
            percentage = percentage.absoluteValue.toFormattedPercentage(),
            state = state
        )
    }
}
