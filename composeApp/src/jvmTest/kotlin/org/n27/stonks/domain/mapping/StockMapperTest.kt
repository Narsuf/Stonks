package org.n27.stonks.domain.mapping

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.n27.stonks.domain.models.Rating
import org.n27.stonks.domain.models.Stocks.Stock.EarningsEstimate
import org.n27.stonks.domain.models.Stocks.Stock.IncomeStatement
import org.n27.stonks.test_data.data.getBalanceSheetRaw
import org.n27.stonks.test_data.data.getStockRaw
import org.n27.stonks.test_data.data.getValuationMeasuresRaw
import org.n27.stonks.test_data.domain.getEarningsEstimate
import org.n27.stonks.test_data.domain.getIncomeStatement
import org.n27.stonks.test_data.domain.getStock

class StockMapperTest {

    @Test
    fun `mapToStock should map to Stock correctly`() {
        val raw = getStockRaw()

        val result = mapStock(
            price = raw.price,
            dividendYield = raw.dividends?.dividendYield,
            payoutRatio = raw.dividends?.payoutRatio,
            incomeStatement = getIncomeStatement(),
            earningsEstimate = getEarningsEstimate(),
            pe = raw.valuationMeasures?.pe,
            valuationFloor = raw.valuationMeasures?.valuationFloor,
            intrinsicValue = raw.valuationMeasures?.intrinsicValue,
            totalCashPerShare = raw.balanceSheet?.totalCashPerShare,
            de = raw.balanceSheet?.de,
            currentRatio = raw.balanceSheet?.currentRatio,
            roe = raw.roe,
            profitMargin = raw.profitMargin,
        )

        assertEquals(getStock(), result)
    }

    // region pe rating

    @Test
    fun `pe rating should be DANGER when negative`() {
        assertEquals(Rating.DANGER, mapStock(pe = -1.0).valuationMeasures?.pe?.rating)
    }

    @Test
    fun `pe rating should be CAUTION when between 0 and 5`() {
        assertEquals(Rating.CAUTION, mapStock(pe = 2.5).valuationMeasures?.pe?.rating)
    }

    @Test
    fun `pe rating should be null when between 5 and 20`() {
        assertNull(mapStock(pe = 12.0).valuationMeasures?.pe?.rating)
    }

    @Test
    fun `pe rating should be CAUTION when between 20 and 25`() {
        assertEquals(Rating.CAUTION, mapStock(pe = 22.0).valuationMeasures?.pe?.rating)
    }

    @Test
    fun `pe rating should be WARNING when between 25 and 30`() {
        assertEquals(Rating.WARNING, mapStock(pe = 27.0).valuationMeasures?.pe?.rating)
    }

    @Test
    fun `pe rating should be DANGER when above 30`() {
        assertEquals(Rating.DANGER, mapStock(pe = 35.0).valuationMeasures?.pe?.rating)
    }

    // endregion

    // region de rating

    @Test
    fun `de rating should be DANGER when negative`() {
        assertEquals(Rating.DANGER, mapStock(de = -1.0).balanceSheet?.de?.rating)
    }

    @Test
    fun `de rating should be POSITIVE when below 1`() {
        assertEquals(Rating.POSITIVE, mapStock(de = 0.5).balanceSheet?.de?.rating)
    }

    @Test
    fun `de rating should be null when between 1 and 2`() {
        assertNull(mapStock(de = 1.5).balanceSheet?.de?.rating)
    }

    @Test
    fun `de rating should be CAUTION when between 2 and 3`() {
        assertEquals(Rating.CAUTION, mapStock(de = 2.5).balanceSheet?.de?.rating)
    }

    @Test
    fun `de rating should be DANGER when above 3`() {
        assertEquals(Rating.DANGER, mapStock(de = 4.0).balanceSheet?.de?.rating)
    }

    // endregion

    // region currentRatio rating

    @Test
    fun `currentRatio rating should be CAUTION when below 0_5`() {
        assertEquals(Rating.CAUTION, mapStock(currentRatio = 0.3).balanceSheet?.currentRatio?.rating)
    }

    @Test
    fun `currentRatio rating should be null when between 0_5 and 1`() {
        assertNull(mapStock(currentRatio = 0.75).balanceSheet?.currentRatio?.rating)
    }

    @Test
    fun `currentRatio rating should be POSITIVE when above 1`() {
        assertEquals(Rating.POSITIVE, mapStock(currentRatio = 1.5).balanceSheet?.currentRatio?.rating)
    }

    // endregion

    // region roe rating

    @Test
    fun `roe rating should be DANGER when negative`() {
        assertEquals(Rating.DANGER, mapStock(roe = -5.0).roe?.rating)
    }

    @Test
    fun `roe rating should be CAUTION when between 0 and 8`() {
        assertEquals(Rating.CAUTION, mapStock(roe = 5.0).roe?.rating)
    }

    @Test
    fun `roe rating should be null when between 8 and 15`() {
        assertNull(mapStock(roe = 12.0).roe?.rating)
    }

    @Test
    fun `roe rating should be POSITIVE when above 15`() {
        assertEquals(Rating.POSITIVE, mapStock(roe = 20.0).roe?.rating)
    }

    // endregion

    // region profitMargin rating

    @Test
    fun `profitMargin rating should be DANGER when negative`() {
        assertEquals(Rating.DANGER, mapStock(profitMargin = -5.0).profitMargin?.rating)
    }

    @Test
    fun `profitMargin rating should be CAUTION when between 0 and 5`() {
        assertEquals(Rating.CAUTION, mapStock(profitMargin = 3.0).profitMargin?.rating)
    }

    @Test
    fun `profitMargin rating should be null when between 5 and 15`() {
        assertNull(mapStock(profitMargin = 10.0).profitMargin?.rating)
    }

    @Test
    fun `profitMargin rating should be POSITIVE when above 15`() {
        assertEquals(Rating.POSITIVE, mapStock(profitMargin = 20.0).profitMargin?.rating)
    }

    // endregion

    // region peg rating

    @Test
    fun `peg rating should be null when below 1_5`() {
        assertNull(mapStock(pe = 10.0, earningsEstimate = EarningsEstimate(growthHigh = null, growthAvg = 10.0)).computed?.peg?.rating)
    }

    @Test
    fun `peg rating should be CAUTION when between 1_5 and 2`() {
        assertEquals(Rating.CAUTION, mapStock(pe = 17.5, earningsEstimate = EarningsEstimate(growthHigh = null, growthAvg = 10.0)).computed?.peg?.rating)
    }

    @Test
    fun `peg rating should be WARNING when between 2 and 3`() {
        assertEquals(Rating.WARNING, mapStock(pe = 25.0, earningsEstimate = EarningsEstimate(growthHigh = null, growthAvg = 10.0)).computed?.peg?.rating)
    }

    @Test
    fun `peg rating should be DANGER when above 3`() {
        assertEquals(Rating.DANGER, mapStock(pe = 40.0, earningsEstimate = EarningsEstimate(growthHigh = null, growthAvg = 10.0)).computed?.peg?.rating)
    }

    // endregion

    // region dynamicPayback rating

    @Test
    fun `dynamicPayback rating should be null when below 15`() {
        assertNull(mapStock(price = 100.0, incomeStatement = getIncomeStatement(eps = 10.0), earningsEstimate = EarningsEstimate(growthHigh = null, growthAvg = 10.0)).computed?.dynamicPayback?.rating)
    }

    @Test
    fun `dynamicPayback rating should be CAUTION when between 15 and 20`() {
        assertEquals(Rating.CAUTION, mapStock(price = 259.37, incomeStatement = getIncomeStatement(eps = 7.47), earningsEstimate = getEarningsEstimate()).computed?.dynamicPayback?.rating)
    }

    @Test
    fun `dynamicPayback rating should be WARNING when between 20 and 25`() {
        assertEquals(Rating.WARNING, mapStock(price = 500.0, incomeStatement = getIncomeStatement(eps = 7.47), earningsEstimate = getEarningsEstimate()).computed?.dynamicPayback?.rating)
    }

    @Test
    fun `dynamicPayback rating should be DANGER when above 25`() {
        assertEquals(Rating.DANGER, mapStock(price = 700.0, incomeStatement = getIncomeStatement(eps = 7.47), earningsEstimate = getEarningsEstimate()).computed?.dynamicPayback?.rating)
    }

    // endregion

    // region cashToEarnings rating

    @Test
    fun `cashToEarnings rating should be CAUTION when below 1`() {
        assertEquals(Rating.CAUTION, mapStock(totalCashPerShare = 4.557, incomeStatement = getIncomeStatement(eps = 7.47)).computed?.cashToEarnings?.rating)
    }

    @Test
    fun `cashToEarnings rating should be null when above 1`() {
        assertNull(mapStock(totalCashPerShare = 10.0, incomeStatement = getIncomeStatement(eps = 7.47)).computed?.cashToEarnings?.rating)
    }

    // endregion

    @Test
    fun `mapToStock should return null earningsYield when pe is zero`() {
        val result = mapStock(pe = 0.0)

        assertNull(result.computed?.earningsYield)
    }

    @Test
    fun `mapToStock should return null peg when growth is null`() {
        val result = mapStock(
            pe = getValuationMeasuresRaw().pe,
            earningsEstimate = null,
        )

        assertNull(result.computed?.peg)
    }

    @Test
    fun `mapToStock should return null peg when growth is negative`() {
        val result = mapStock(
            pe = getValuationMeasuresRaw().pe,
            earningsEstimate = EarningsEstimate(growthHigh = -1.0, growthAvg = null),
        )

        assertNull(result.computed?.peg)
    }

    @Test
    fun `mapToStock should return null dynamicPayback when eps is zero`() {
        val result = mapStock(
            incomeStatement = getIncomeStatement(eps = 0.0),
            earningsEstimate = getEarningsEstimate(),
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
        earningsEstimate: EarningsEstimate? = null,
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
            earningsEstimate = earningsEstimate,
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
