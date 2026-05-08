package org.n27.stonks.data.remote.eurostat.model

import kotlinx.serialization.Serializable

@Serializable
internal data class EurostatResponse(
    val value: Map<String, Double>,
    val dimension: Dimension,
) {

    @Serializable
    data class Dimension(val time: Time) {

        @Serializable
        data class Time(val category: Category) {

            @Serializable
            data class Category(val index: Map<String, Int>)
        }
    }
}
