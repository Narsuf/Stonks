package org.n27.stonks.data.remote.bundesbank.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class BundesbankResponse(
    val data: Data,
) {

    @Serializable
    data class Data(
        val structure: Structure,
        val dataSets: List<DataSet>,
    )

    @Serializable
    data class Structure(val dimensions: Dimensions)

    @Serializable
    data class Dimensions(val observation: List<Observation>)

    @Serializable
    data class Observation(val values: List<Value>)

    @Serializable
    data class Value(val id: String)

    @Serializable
    data class DataSet(val series: Map<String, Series>)

    @Serializable
    data class Series(val observations: Map<String, List<JsonElement>>)
}
