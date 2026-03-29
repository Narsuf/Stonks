package org.n27.stonks.domain.mapping

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.n27.stonks.domain.models.RatedValue
import org.n27.stonks.domain.models.Stocks.Stock.*
import org.n27.stonks.test_data.data.getStockRaw
import org.n27.stonks.test_data.domain.*

class StockMapperTest {

    @Test
    fun `mapToStock should map to Stock correctly`() {
        val raw = getStockRaw()

        val result = mapStock(
            price = raw.price,
            dividendYield = raw.dividends?.dividendYield,
            payoutRatio = raw.dividends?.payoutRatio,
            incomeStatement = getIncomeStatement(),
            analysis = getAnalysis(),
            valuationMeasures = getValuationMeasures(),
            balanceSheet = getBalanceSheet(),
            roe = raw.roe,
            profitMargin = raw.profitMargin,
        )

        assertEquals(getStock(), result)
    }

    @Test
    fun `mapToStock should return null earningsYield when pe is zero`() {
        val result = mapStock(valuationMeasures = getValuationMeasures(pe = RatedValue(0.0, null)))

        assertNull(result.computed?.earningsYield)
    }

    @Test
    fun `mapToStock should return null peg when growth is null`() {
        val result = mapStock(
            valuationMeasures = getValuationMeasures(),
            analysis = getAnalysis(earningsEstimate = null),
        )

        assertNull(result.computed?.peg)
    }

    @Test
    fun `mapToStock should return null peg when growth is negative`() {
        val result = mapStock(
            valuationMeasures = getValuationMeasures(),
            analysis = getAnalysis(earningsEstimate = Analysis.EarningsEstimate(growthLow = null, growthHigh = -1.0, growthAvg = null)),
        )

        assertNull(result.computed?.peg)
    }

    @Test
    fun `mapToStock should return null dynamicPayback when eps is zero`() {
        val result = mapStock(
            incomeStatement = getIncomeStatement(eps = 0.0),
            analysis = getAnalysis(),
        )

        assertNull(result.computed?.dynamicPayback)
    }

    @Test
    fun `mapToStock should return null cashToEarnings when eps is zero`() {
        val result = mapStock(
            incomeStatement = getIncomeStatement(eps = 0.0),
            balanceSheet = getBalanceSheet(),
        )

        assertNull(result.computed?.cashToEarnings)
    }

    @Test
    fun `mapToStock should return null cashToPrice when price is zero`() {
        val result = mapStock(
            price = 0.0,
            balanceSheet = getBalanceSheet(),
        )

        assertNull(result.computed?.cashToPrice)
    }

    private fun mapStock(
        price: Double? = null,
        dividendYield: Double? = null,
        payoutRatio: Double? = null,
        incomeStatement: IncomeStatement? = null,
        analysis: Analysis? = null,
        valuationMeasures: ValuationMeasures? = null,
        balanceSheet: BalanceSheet? = null,
        roe: Double? = null,
        profitMargin: Double? = null,
    ) = getStockRaw().let { raw ->
        mapToStock(
            symbol = raw.symbol,
            companyName = raw.companyName,
            logo = raw.logo,
            price = price,
            currency = raw.currency,
            lastUpdated = raw.lastUpdated,
            isWatchlisted = raw.isWatchlisted,
            dividendYield = dividendYield,
            payoutRatio = payoutRatio,
            incomeStatement = incomeStatement,
            analysis = analysis,
            valuationMeasures = valuationMeasures,
            balanceSheet = balanceSheet,
            roe = roe,
            profitMargin = profitMargin,
        )
    }
}
