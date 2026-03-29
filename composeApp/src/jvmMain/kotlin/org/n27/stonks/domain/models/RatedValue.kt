package org.n27.stonks.domain.models

data class RatedValue(
    val value: Double,
    val rating: Rating?,
)

enum class Rating {
    POSITIVE,
    CAUTION,
    WARNING,
    DANGER,
}
