package org.n27.stonks.domain.models

data class MacroIndicators(
    val treasury10Y: Double,
    val europeanTreasury10Y: Double,
    val corporateAAA: Double,
    val germanCpi: Double?,
)
