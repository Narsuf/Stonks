package org.n27.stonks.domain.mapping

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.n27.stonks.domain.model.Rating
import org.n27.stonks.domain.model.Stocks.Stock.EarningsEstimate
import org.n27.stonks.domain.model.Stocks.Stock.IncomeStatement
import org.n27.stonks.test_data.data.getBalanceSheetRaw
import org.n27.stonks.test_data.data.getStockRaw
import org.n27.stonks.test_data.data.getValuationMeasuresRaw
import org.n27.stonks.test_data.domain.getEarningsEstimate
import org.n27.stonks.test_data.domain.getIncomeStatement
import org.n27.stonks.test_data.domain.getStock
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

    // region ratings

    @ParameterizedTest(name = "pe={0} → {1}")
    @MethodSource("peRatingCases")
    fun `pe rating`(pe: Double, expected: Rating?) {
        assertEquals(expected, mapStock(pe = pe).valuationMeasures?.pe?.rating)
    }

    @ParameterizedTest(name = "de={0} → {1}")
    @MethodSource("deRatingCases")
    fun `de rating`(de: Double, expected: Rating?) {
        assertEquals(expected, mapStock(de = de).balanceSheet?.de?.rating)
    }

    @ParameterizedTest(name = "currentRatio={0} → {1}")
    @MethodSource("currentRatioRatingCases")
    fun `currentRatio rating`(currentRatio: Double, expected: Rating?) {
        assertEquals(expected, mapStock(currentRatio = currentRatio).balanceSheet?.currentRatio?.rating)
    }

    @ParameterizedTest(name = "roe={0} → {1}")
    @MethodSource("roeRatingCases")
    fun `roe rating`(roe: Double, expected: Rating?) {
        assertEquals(expected, mapStock(roe = roe).roe?.rating)
    }

    @ParameterizedTest(name = "profitMargin={0} → {1}")
    @MethodSource("profitMarginRatingCases")
    fun `profitMargin rating`(profitMargin: Double, expected: Rating?) {
        assertEquals(expected, mapStock(profitMargin = profitMargin).profitMargin?.rating)
    }

    @ParameterizedTest(name = "pe={0} → peg={1}")
    @MethodSource("pegRatingCases")
    fun `peg rating`(pe: Double, expected: Rating?) {
        val result = mapStock(pe = pe, earningsEstimate = EarningsEstimate(growthHigh = 10.0))
        assertEquals(expected, result.computed?.peg?.rating)
    }

    @ParameterizedTest(name = "price={0}, eps={1} → dynamicPayback={3}")
    @MethodSource("dynamicPaybackRatingCases")
    fun `dynamicPayback rating`(price: Double, eps: Double, growthHigh: Double, expected: Rating?) {
        val result = mapStock(
            price = price,
            incomeStatement = getIncomeStatement(eps = eps),
            earningsEstimate = EarningsEstimate(growthHigh = growthHigh),
        )
        assertEquals(expected, result.computed?.dynamicPayback?.rating)
    }

    @ParameterizedTest(name = "totalCashPerShare={0} → {1}")
    @MethodSource("cashToEarningsRatingCases")
    fun `cashToEarnings rating`(totalCashPerShare: Double, expected: Rating?) {
        val result = mapStock(totalCashPerShare = totalCashPerShare, incomeStatement = getIncomeStatement(eps = 7.47))
        assertEquals(expected, result.computed?.cashToEarnings?.rating)
    }

    // endregion

    @Test
    fun `mapToStock should return null earningsYield when pe is zero`() {
        assertNull(mapStock(pe = 0.0).computed?.earningsYield)
    }

    @Test
    fun `mapToStock should return null peg when growth is null`() {
        assertNull(mapStock(pe = getValuationMeasuresRaw().pe, earningsEstimate = null).computed?.peg)
    }

    @Test
    fun `mapToStock should return null peg when growth is negative`() {
        assertNull(mapStock(pe = getValuationMeasuresRaw().pe, earningsEstimate = EarningsEstimate(growthHigh = -1.0)).computed?.peg)
    }

    @Test
    fun `mapToStock should return null dynamicPayback when eps is zero`() {
        assertNull(mapStock(incomeStatement = getIncomeStatement(eps = 0.0), earningsEstimate = getEarningsEstimate()).computed?.dynamicPayback)
    }

    @Test
    fun `mapToStock should return null cashToEarnings when eps is zero`() {
        assertNull(mapStock(incomeStatement = getIncomeStatement(eps = 0.0), totalCashPerShare = getBalanceSheetRaw().totalCashPerShare).computed?.cashToEarnings)
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

    companion object {
        @JvmStatic
        fun peRatingCases() = listOf(
            Arguments.of(-1.0, Rating.DANGER),
            Arguments.of(2.5, Rating.CAUTION),
            Arguments.of(12.0, null),
            Arguments.of(22.0, Rating.CAUTION),
            Arguments.of(27.0, Rating.WARNING),
            Arguments.of(35.0, Rating.DANGER),
        )

        @JvmStatic
        fun deRatingCases() = listOf(
            Arguments.of(-1.0, Rating.DANGER),
            Arguments.of(0.5, Rating.POSITIVE),
            Arguments.of(1.5, null),
            Arguments.of(2.5, Rating.CAUTION),
            Arguments.of(4.0, Rating.DANGER),
        )

        @JvmStatic
        fun currentRatioRatingCases() = listOf(
            Arguments.of(0.3, Rating.CAUTION),
            Arguments.of(0.75, null),
            Arguments.of(1.5, Rating.POSITIVE),
        )

        @JvmStatic
        fun roeRatingCases() = listOf(
            Arguments.of(-5.0, Rating.DANGER),
            Arguments.of(5.0, Rating.CAUTION),
            Arguments.of(12.0, null),
            Arguments.of(20.0, Rating.POSITIVE),
        )

        @JvmStatic
        fun profitMarginRatingCases() = listOf(
            Arguments.of(-5.0, Rating.DANGER),
            Arguments.of(3.0, Rating.CAUTION),
            Arguments.of(10.0, null),
            Arguments.of(20.0, Rating.POSITIVE),
        )

        @JvmStatic
        fun pegRatingCases() = listOf(
            Arguments.of(10.0, null),
            Arguments.of(17.5, Rating.CAUTION),
            Arguments.of(25.0, Rating.WARNING),
            Arguments.of(40.0, Rating.DANGER),
        )

        @JvmStatic
        fun dynamicPaybackRatingCases() = listOf(
            Arguments.of(100.0, 10.0, 10.0, null),
            Arguments.of(259.37, 7.47, 8.65, Rating.CAUTION),
            Arguments.of(500.0, 7.47, 8.65, Rating.WARNING),
            Arguments.of(700.0, 7.47, 8.65, Rating.DANGER),
        )

        @JvmStatic
        fun cashToEarningsRatingCases() = listOf(
            Arguments.of(4.557, Rating.CAUTION),
            Arguments.of(10.0, null),
        )
    }
}
