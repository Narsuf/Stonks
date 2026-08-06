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
    val earningsEstimate: MetricValueRaw?,
    val valuationMeasures: ValuationMeasuresRaw?,
    val balanceSheet: BalanceSheetRaw?,
    val roe: MetricValueRaw?,
    val profitMargin: MetricValueRaw?,
    val computed: ComputedRaw?,
    val currency: String?,
    val lastUpdated: Long?,
    val isWatchlisted: Boolean,
)

@Serializable
data class DividendsRaw(
    val dividendYield: MetricValueRaw?,
    val payoutRatio: MetricValueRaw?,
)

@Serializable
data class ComputedRaw(
    val earningsYield: Double?,
    val peg: MetricValueRaw?,
    val dynamicPayback: MetricValueRaw?,
)

@Serializable
data class IncomeStatementRaw(
    val eps: MetricValueRaw?,
    val earningsQuarterlyGrowth: MetricValueRaw?,
)

@Serializable
data class ValuationMeasuresRaw(
    val pe: MetricValueRaw?,
    val valuationFloor: Double?,
    val intrinsicValue: Double?,
)

@Serializable
data class BalanceSheetRaw(
    val de: MetricValueRaw?,
    val totalCashPerShare: MetricValueRaw?,
)

@Serializable
data class MetricValueRaw(
    val value: Double,
    val rating: RatingRaw?,
    val variation: Double? = null,
)

@Serializable
enum class RatingRaw {
    POSITIVE,
    CAUTION,
    WARNING,
    DANGER,
}
