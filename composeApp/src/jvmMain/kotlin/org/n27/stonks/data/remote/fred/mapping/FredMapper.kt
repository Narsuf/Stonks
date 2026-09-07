package org.n27.stonks.data.remote.fred.mapping

import org.n27.stonks.data.remote.fred.model.FredObservationsResponse
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator

internal fun FredObservationsResponse.toDomain(): MacroIndicator {
    val (date, value) = observations.firstNotNullOfOrNull { observation ->
        observation.value.toDoubleOrNull()?.let { observation.date to it }
    } ?: error("FRED response contained no numeric values")

    return MacroIndicator(value, date)
}
