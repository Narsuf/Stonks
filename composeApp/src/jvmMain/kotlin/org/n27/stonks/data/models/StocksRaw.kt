package org.n27.stonks.data.models

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
    val analysis: AnalysisRaw?,
    val valuationMeasures: ValuationMeasuresRaw?,
    val balanceSheet: BalanceSheetRaw?,
    val roe: Double?,
    val profitMargin: Double?,
    val currency: String?,
    val lastUpdated: Long?,
    val isWatchlisted: Boolean,
)

@Serializable
data class DividendsRaw(
    val dividendYield: Double?,
    val payoutRatio: Double?,
)

@Serializable
data class IncomeStatementRaw(
    val eps: Double?,
    val earningsQuarterlyGrowth: Double?,
    val revenueQuarterlyGrowth: Double?,
)

@Serializable
data class AnalysisRaw(
    val earningsEstimate: EarningsEstimateRaw?,
    val revenueEstimate: RevenueEstimateRaw?,
)

@Serializable
data class EarningsEstimateRaw(
    val growthLow: Double?,
    val growthHigh: Double?,
)

@Serializable
data class RevenueEstimateRaw(
    val growthLow: Double?,
    val growthHigh: Double?,
)

@Serializable
data class ValuationMeasuresRaw(
    val pe: Double?,
    val valuationFloor: Double?,
    val intrinsicValue: Double?,
)

@Serializable
data class BalanceSheetRaw(
    val totalCashPerShare: Double?,
    val de: Double?,
)
