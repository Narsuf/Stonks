package org.n27.stonks.domain.models

data class Stocks(
    val items: List<Stock>,
    val nextPage: Int? = null,
) {

    data class Stock(
        val symbol: String,
        val companyName: String,
        val logo: String?,
        val price: Double?,
        val dividendYield: Double?,
        val currency: String?,
        val lastUpdated: Long?,
        val isWatchlisted: Boolean,
        val incomeStatement: IncomeStatement?,
        val analysis: Analysis?,
        val valuationMeasures: ValuationMeasures?,
        val balanceSheet: BalanceSheet?,
        val roe: Double?,
        val profitMargin: Double?,
        val computed: Computed?,
    ) {

        data class Computed(
            val earningsYield: Double?,
            val peg: Double?,
            val dynamicPayback: Double?,
            val payoutRatio: Double?,
            val cashToEarnings: Double?,
            val cashToPrice: Double?,
        )

        data class IncomeStatement(
            val eps: Double?,
            val earningsQuarterlyGrowth: Double?,
            val revenueQuarterlyGrowth: Double?,
        )

        data class Analysis(
            val earningsEstimate: EarningsEstimate?,
            val revenueEstimate: RevenueEstimate?,
        ) {

            data class EarningsEstimate(
                val growthLow: Double?,
                val growthHigh: Double?,
            )

            data class RevenueEstimate(
                val growthLow: Double?,
                val growthHigh: Double?,
            )
        }

        data class ValuationMeasures(
            val pe: Double?,
            val valuationFloor: Double?,
            val intrinsicValue: Double?,
        )

        data class BalanceSheet(
            val totalCashPerShare: Double?,
            val de: Double?,
        )
    }
}
