package org.n27.stonks.domain.common

data class Stock(
    val symbol: String,
    val logoUrl: String?,
    val companyName: String,
    val price: Double?,
    val eps: Double?,
    val trailingPe: Double?,
    val forwardPe: Double?,
    val dividendYield: Double?,
    val earningsQuarterlyGrowth: Double?,
    val currency: String?,
    val intrinsicValue: Double?,
)
