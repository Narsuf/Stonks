package org.n27.stonks.data.remote.mapping

import org.n27.stonks.data.remote.model.MacroIndicatorRaw
import org.n27.stonks.domain.model.MacroIndicators
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator

internal fun MacroIndicatorRaw.toDomain() = MacroIndicator(value, date)

internal fun mapToMacroIndicators(
    bundYield: MacroIndicatorRaw,
    germanCpi: MacroIndicatorRaw,
) = MacroIndicators(
    bundYield10Y = bundYield.toDomain(),
    germanCpi = germanCpi.toDomain(),
)
