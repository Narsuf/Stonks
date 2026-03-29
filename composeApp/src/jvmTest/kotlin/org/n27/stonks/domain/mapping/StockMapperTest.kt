package org.n27.stonks.domain.mapping

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.n27.stonks.domain.models.Stocks.Stock.*
import org.n27.stonks.test_data.data.getBalanceSheetRaw
import org.n27.stonks.test_data.data.getStockRaw
import org.n27.stonks.test_data.data.getValuationMeasuresRaw
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
            pe = raw.valuationMeasures?.pe,
            valuationFloor = raw.valuationMeasures?.valuationFloor,
            intrinsicValue = raw.valuationMeasures?.intrinsicValue,
            totalCashPerShare = raw.balanceSheet?.totalCashPerShare,
            de = raw.balanceSheet?.de?.let { it / 100 },
            currentRatio = raw.balanceSheet?.currentRatio,
            roe = raw.roe?.let { it * 100 },
            profitMargin = raw.profitMargin?.let { it * 100 },
        )

        assertEquals(getStock(), result)
    }

    @Test
    fun `mapToStock should return null earningsYield when pe is zero`() {
        val result = mapStock(pe = 0.0)

        assertNull(result.computed?.earningsYield)
    }

    @Test
    fun `mapToStock should return null peg when growth is null`() {
        val result = mapStock(
            pe = getValuationMeasuresRaw().pe,
            analysis = getAnalysis(earningsEstimate = null),
        )

        assertNull(result.computed?.peg)
    }

    @Test
    fun `mapToStock should return null peg when growth is negative`() {
        val result = mapStock(
            pe = getValuationMeasuresRaw().pe,
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
            totalCashPerShare = getBalanceSheetRaw().totalCashPerShare,
        )

        assertNull(result.computed?.cashToEarnings)
    }

    private fun mapStock(
        price: Double? = null,
        dividendYield: Double? = null,
        payoutRatio: Double? = null,
        incomeStatement: IncomeStatement? = null,
        analysis: Analysis? = null,
        pe: Double? = null,
        valuationFloor: Double? = null,
        intrinsicValue: Double? = null,
        totalCashPerShare: Double? = null,
        de: Double? = null,
        currentRatio: Double? = null,
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
            pe = pe,
            valuationFloor = valuationFloor,
            intrinsicValue = intrinsicValue,
            totalCashPerShare = totalCashPerShare,
            de = de,
            currentRatio = currentRatio,
            roe = roe,
            profitMargin = profitMargin,
        )
    }
}
