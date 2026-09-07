package org.n27.stonks.data.remote.fred.model

import kotlinx.serialization.Serializable

@Serializable
data class FredObservationsResponse(
    val observations: List<FredObservation> = emptyList(),
) {

    @Serializable
    data class FredObservation(
        val date: String,
        val value: String,
    )
}
