package org.n27.stonks.test_data.data

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import org.n27.stonks.data.remote.bundesbank.model.BundesbankResponse

fun getBundesbankResponse(value: Double = 1.5, date: String = "2026-01-01") =
    getBundesbankResponse(dates = listOf(date), observations = mapOf("0" to listOf(value)))

fun getBundesbankResponse(dates: List<String>, observations: Map<String, List<Any?>>): BundesbankResponse {
    val jsonObservations = observations.mapValues { (_, values) ->
        values.map { if (it == null) JsonNull else JsonPrimitive(it.toString()) }
    }
    return BundesbankResponse(
        data = BundesbankResponse.Data(
            structure = BundesbankResponse.Structure(
                dimensions = BundesbankResponse.Dimensions(
                    observation = listOf(
                        BundesbankResponse.Observation(dates.map { BundesbankResponse.Value(it) })
                    )
                )
            ),
            dataSets = listOf(
                BundesbankResponse.DataSet(
                    series = mapOf("0:0:0" to BundesbankResponse.Series(jsonObservations))
                )
            ),
        )
    )
}
