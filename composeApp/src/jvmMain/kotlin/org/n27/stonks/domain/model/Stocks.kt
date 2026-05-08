package org.n27.stonks.domain.model

data class Stocks(
    val items: List<Stock>,
    val nextPage: Int? = null,
) {

    data class Stock(
        val symbol: String,
        val companyName: String,
        val logo: String?,
        val price: Double?,
        val dividends: Dividends?,
        val currency: String?,
        val lastUpdated: Long?,
        val isWatchlisted: Boolean,
        val incomeStatement: IncomeStatement?,
        val earningsEstimate: EarningsEstimate?,
        val valuationMeasures: ValuationMeasures?,
        val balanceSheet: BalanceSheet?,
        val roe: RatedValue?,
        val profitMargin: RatedValue?,
        val computed: Computed?,
    ) {

        data class Dividends(
            val dividendYield: Double?,
            val payoutRatio: Double?,
        )

        data class Computed(
            val earningsYield: Double?,
            val peg: RatedValue?,
            val dynamicPayback: RatedValue?,
            val cashToEarnings: RatedValue?,
        )

        data class IncomeStatement(
            val eps: Double?,
            val earningsQuarterlyGrowth: Double?,
        )

        data class EarningsEstimate(
            val growthHigh: Double?,
        )

        data class ValuationMeasures(
            val pe: RatedValue?,
            val valuationFloor: Double?,
            val intrinsicValue: Double?,
        )

        data class BalanceSheet(
            val totalCashPerShare: Double?,
            val de: RatedValue?,
            val currentRatio: RatedValue?,
        )
    }
}
