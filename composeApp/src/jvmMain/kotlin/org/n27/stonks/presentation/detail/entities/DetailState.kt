package org.n27.stonks.presentation.detail.entities

internal sealed class DetailState {

    data object Idle : DetailState()
    data object Loading: DetailState()
    data object Error: DetailState()

    data class Content(
        val symbol: String,
        val logoUrl: String,
        val name: String,
        val price: String?,
        val eps: String?,
        val trailingPe: String?,
        val forwardPe: String?,
        val dividendYield: String?,
        val earningsQuarterlyGrowth: String?,
        val intrinsicValue: String?,
    ) : DetailState()
}
