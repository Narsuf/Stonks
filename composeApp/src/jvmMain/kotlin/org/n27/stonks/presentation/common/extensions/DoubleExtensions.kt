package org.n27.stonks.presentation.common.extensions

import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import java.math.RoundingMode
import kotlin.math.absoluteValue

internal fun Double.toFormattedBigDecimal() = toString()
    .toBigDecimal()
    .setScale(2, RoundingMode.HALF_UP)

internal fun Double.toFormattedString() = toFormattedBigDecimal().toPlainString()

internal fun Double.toFormattedPercentage() = "${toFormattedString()} %"

internal fun Double.getTargetPrice(
    eps: Double?,
    expectedEpsGrowth: Double?
): DeltaTextEntity? {
    if (eps == null || expectedEpsGrowth == null) return null

    val targetPrice = (expectedEpsGrowth * 2 + 8.5) * eps
    val priceDiff = targetPrice - this

    val percentage = if (targetPrice != 0.0)
        (priceDiff / targetPrice) * 100
    else
        0.0

    val state = when {
        priceDiff > 0 -> DeltaState.POSITIVE
        priceDiff < 0 -> DeltaState.NEGATIVE
        else -> DeltaState.NEUTRAL
    }

    return DeltaTextEntity(
        value =  priceDiff.absoluteValue.toFormattedString(),
        percentage = percentage.absoluteValue.toFormattedPercentage(),
        state = state
    )
}
