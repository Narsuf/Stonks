package org.n27.stonks.domain.model

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
