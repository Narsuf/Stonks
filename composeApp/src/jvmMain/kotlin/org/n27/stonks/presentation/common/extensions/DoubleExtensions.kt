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

internal fun Double.getTargetPrice(
    intrinsicValue: Double?,
    currency: String?,
): DeltaTextEntity? {
    if (intrinsicValue == null) return null
    return getDelta(diff = intrinsicValue - this, base = this) { it.toPrice(currency) }
}

internal fun Double.getVariationDelta(
    variation: Double?,
    formatValue: (Double) -> String?,
): DeltaTextEntity? {
    if (variation == null) return null
    return getDelta(diff = variation, base = this - variation, formatValue = formatValue)
}

private fun getDelta(
    diff: Double,
    base: Double,
    formatValue: (Double) -> String?,
): DeltaTextEntity? {
    val percentage = if (base != 0.0)
        (diff / base) * 100
    else
        0.0

    val state = when {
        diff > 0 -> DeltaState.POSITIVE
        diff < 0 -> DeltaState.NEGATIVE
        else -> DeltaState.NEUTRAL
    }

    return formatValue(diff.absoluteValue)?.let {
        DeltaTextEntity(
            value = it,
            percentage = percentage.absoluteValue.toFormattedPercentage(),
            state = state,
        )
    }
}
