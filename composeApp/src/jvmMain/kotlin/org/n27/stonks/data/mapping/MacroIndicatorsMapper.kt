package org.n27.stonks.data.mapping

import org.n27.stonks.data.models.MacroIndicatorRaw
import org.n27.stonks.domain.models.MacroIndicators

internal fun MacroIndicatorRaw.toDomain() = MacroIndicators.MacroIndicator(value, date)

internal fun mapToMacroIndicators(
    treasury: MacroIndicatorRaw,
    europeanTreasury: MacroIndicatorRaw,
    corporate: MacroIndicatorRaw,
    germanCpi: MacroIndicatorRaw,
) = MacroIndicators(
    treasury10Y = treasury.toDomain(),
    europeanTreasury10Y = europeanTreasury.toDomain(),
    corporateAAA = corporate.toDomain(),
    germanCpi = germanCpi.toDomain(),
)
