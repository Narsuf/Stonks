package org.n27.stonks.domain.models

data class MacroIndicators(
    val treasury10Y: MacroIndicator,
    val europeanTreasury10Y: MacroIndicator,
    val corporateAAA: MacroIndicator,
    val germanCpi: MacroIndicator,
) {

    data class MacroIndicator(
        val value: Double,
        val date: String,
    )
}
