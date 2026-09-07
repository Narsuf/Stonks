package org.n27.stonks.test_data.data

import org.n27.stonks.data.remote.fred.model.FredObservationsResponse
import org.n27.stonks.data.remote.fred.model.FredObservationsResponse.FredObservation

fun getFredObservationsResponse(value: Double = 1.5, date: String = "2026-01-01") =
    getFredObservationsResponse(listOf(date to value.toString()))

fun getFredObservationsResponse(observations: List<Pair<String, String>>) = FredObservationsResponse(
    observations = observations.map { (date, value) -> FredObservation(date, value) },
)
