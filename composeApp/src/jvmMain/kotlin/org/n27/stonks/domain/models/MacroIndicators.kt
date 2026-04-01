package org.n27.stonks.domain.models

data class MacroIndicators(
    val treasury10Y: Double,
    val treasury10YDate: String?,
    val europeanTreasury10Y: Double,
    val europeanTreasury10YDate: String?,
    val corporateAAA: Double,
    val germanCpi: Double?,
    val germanCpiDate: String?,
)
