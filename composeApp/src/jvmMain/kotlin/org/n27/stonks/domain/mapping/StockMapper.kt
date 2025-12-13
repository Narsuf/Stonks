package org.n27.stonks.domain.mapping

import org.n27.stonks.domain.models.Stock

internal fun Stock.recalculateIntrinsicValues(growth: Double, valuationFloor: Double?): Stock {
    var returnValue = copy(
        expectedEpsGrowth = growth,
        forwardIntrinsicValue = eps?.getIntrinsicValue(valuationFloor ?: 16.0, growth),
    )

    if (eps != null && valuationFloor != null) {
        returnValue = returnValue.copy(
            valuationFloor = valuationFloor,
            currentIntrinsicValue = eps.getIntrinsicValue(valuationFloor),
        )
    }

    return returnValue
}

private fun Double.getIntrinsicValue(
    valuationFloor: Double,
    expectedEpsGrowth: Double = 0.0,
) = expectedEpsGrowth.toMultiplier() * valuationFloor * this

private fun Double.toMultiplier(): Double = 1 + this / 100
