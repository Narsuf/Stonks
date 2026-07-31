package org.n27.stonks.data.remote.bundesbank.mapping

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import org.n27.stonks.data.remote.bundesbank.model.BundesbankResponse
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator

internal fun BundesbankResponse.toDomain(): MacroIndicator {
    val series = data.dataSets.first().series.values.first()
    val dates = data.structure.dimensions.observation.first().values

    val (index, value) = series.observations.entries
        .mapNotNull { (key, observation) ->
            val first = observation.firstOrNull()
            if (first == null || first is JsonNull) null
            else key.toInt() to (first as JsonPrimitive).content.toDouble()
        }
        .maxByOrNull { (index, _) -> index }
        ?: error("Bundesbank response contained no numeric values")

    return MacroIndicator(value, dates[index].id)
}
