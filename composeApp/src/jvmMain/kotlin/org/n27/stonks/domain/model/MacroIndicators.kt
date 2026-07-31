package org.n27.stonks.domain.model

data class MacroIndicators(
    val bundYield10Y: MacroIndicator,
    val germanCpi: MacroIndicator,
) {

    data class MacroIndicator(
        val value: Double,
        val date: String,
    )
}
