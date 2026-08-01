package org.n27.stonks.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StocksRaw(
    val items: List<StockRaw>,
    val nextPage: Int? = null,
)

@Serializable
data class StockRaw(
    val symbol: String,
    val companyName: String,
    val logo: String?,
    val price: Double?,
    val dividends: DividendsRaw?,
    val incomeStatement: IncomeStatementRaw?,
    val earningsEstimate: RatedValueRaw?,
    val valuationMeasures: ValuationMeasuresRaw?,
    val balanceSheet: BalanceSheetRaw?,
    val roe: RatedValueRaw?,
    val profitMargin: RatedValueRaw?,
    val computed: ComputedRaw?,
    val currency: String?,
    val lastUpdated: Long?,
    val isWatchlisted: Boolean,
)

@Serializable
data class DividendsRaw(
    val dividendYield: Double?,
    val payoutRatio: RatedValueRaw?,
)

@Serializable
data class ComputedRaw(
    val earningsYield: Double?,
    val peg: RatedValueRaw?,
    val dynamicPayback: RatedValueRaw?,
)

@Serializable
data class IncomeStatementRaw(
    val eps: Double?,
    val earningsQuarterlyGrowth: Double?,
)

@Serializable
data class ValuationMeasuresRaw(
    val pe: RatedValueRaw?,
    val valuationFloor: Double?,
    val intrinsicValue: Double?,
)

@Serializable
data class BalanceSheetRaw(
    val de: RatedValueRaw?,
    val totalCashPerShare: Double?,
)

@Serializable
data class RatedValueRaw(
    val value: Double,
    val rating: RatingRaw?,
)

@Serializable
enum class RatingRaw {
    POSITIVE,
    CAUTION,
    WARNING,
    DANGER,
}
