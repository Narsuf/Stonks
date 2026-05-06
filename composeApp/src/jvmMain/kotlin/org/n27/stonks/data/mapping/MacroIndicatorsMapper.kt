package org.n27.stonks.data.mapping

import org.n27.stonks.data.model.MacroIndicatorRaw
import org.n27.stonks.domain.model.MacroIndicators
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator

internal fun MacroIndicatorRaw.toDomain() = MacroIndicator(value, date)

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
