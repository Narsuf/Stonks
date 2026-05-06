package org.n27.stonks.domain.model

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
