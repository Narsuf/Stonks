package org.n27.stonks.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class FredObservationsResponse(
    val observations: List<FredObservation> = emptyList(),
) {

    @Serializable
    data class FredObservation(
        val date: String,
        val value: String,
    )
}

