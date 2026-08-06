package org.n27.stonks.domain.model

data class MetricValue(
    val value: Double,
    val rating: Rating?,
    val variation: Double? = null,
)

enum class Rating {
    POSITIVE,
    CAUTION,
    WARNING,
    DANGER,
}
