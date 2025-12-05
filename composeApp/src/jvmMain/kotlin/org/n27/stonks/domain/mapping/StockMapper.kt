package org.n27.stonks.domain.mapping

import org.n27.stonks.domain.models.Stock

internal fun Stock.addExpectedEpsGrowth(growth: Double): Stock {
    return copy(
        expectedEpsGrowth = growth,
        forwardIntrinsicValue = eps?.getIntrinsicValue(growth),
    )
}

private fun Double.getIntrinsicValue(expectedEpsGrowth: Double = 0.0) = expectedEpsGrowth.toMultiplier() * 12.5 * this

private fun Double.toMultiplier(): Double = 1 + this / 100
