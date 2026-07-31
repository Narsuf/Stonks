package org.n27.stonks.domain.mapping

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.n27.stonks.domain.model.Rating
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StockMapperTest {

    // region ratings

    @ParameterizedTest(name = "pe={0} → {1}")
    @MethodSource("peRatingCases")
    fun `pe rating`(pe: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toPeRating(pe))
    }

    @ParameterizedTest(name = "de={0} → {1}")
    @MethodSource("deRatingCases")
    fun `de rating`(de: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toDeRating(de))
    }

    @ParameterizedTest(name = "currentRatio={0} → {1}")
    @MethodSource("currentRatioRatingCases")
    fun `currentRatio rating`(currentRatio: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toCurrentRatioRating(currentRatio))
    }

    @ParameterizedTest(name = "roe={0} → {1}")
    @MethodSource("roeRatingCases")
    fun `roe rating`(roe: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toRoeRating(roe))
    }

    @ParameterizedTest(name = "profitMargin={0} → {1}")
    @MethodSource("profitMarginRatingCases")
    fun `profitMargin rating`(profitMargin: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toProfitMarginRating(profitMargin))
    }

    @ParameterizedTest(name = "growthHigh={0} → {1}")
    @MethodSource("earningsEstimateRatingCases")
    fun `earningsEstimate rating`(growthHigh: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toForwardEarningsGrowthRating(growthHigh))
    }

    @ParameterizedTest(name = "pe={0} → peg={1}")
    @MethodSource("pegRatingCases")
    fun `peg rating`(pe: Double, expected: Rating?) {
        assertEquals(expected, computePeg(pe, growth = 10.0)?.rating)
    }

    @ParameterizedTest(name = "price={0}, eps={1}, growthHigh={2} → dynamicPayback={3}")
    @MethodSource("dynamicPaybackRatingCases")
    fun `dynamicPayback rating`(price: Double, eps: Double, growthHigh: Double, expected: Rating?) {
        assertEquals(expected, computeDynamicPayback(price, eps, growthHigh)?.rating)
    }

    // endregion

    @Test
    fun `mapToStock should return null earningsYield when pe is zero`() {
        assertNull(computeEarningsYield(0.0))
    }

    @Test
    fun `mapToStock should return null peg when growth is null`() {
        assertNull(computePeg(pe = 34.72, growth = null))
    }

    @Test
    fun `mapToStock should return null peg when growth is negative`() {
        assertNull(computePeg(pe = 34.72, growth = -1.0))
    }

    @Test
    fun `mapToStock should return null dynamicPayback when eps is zero`() {
        assertNull(computeDynamicPayback(price = null, eps = 0.0, growth = 11.43))
    }

    companion object {
        @JvmStatic
        fun peRatingCases() = listOf(
            Arguments.of(-1.0, Rating.DANGER),
            Arguments.of(12.0, null),
            Arguments.of(22.0, Rating.CAUTION),
            Arguments.of(27.0, Rating.WARNING),
            Arguments.of(35.0, Rating.WARNING),
        )

        @JvmStatic
        fun deRatingCases() = listOf(
            Arguments.of(0.2, Rating.POSITIVE),
            Arguments.of(0.4, null),
            Arguments.of(0.75, Rating.CAUTION),
            Arguments.of(2.5, Rating.DANGER),
        )

        @JvmStatic
        fun currentRatioRatingCases() = listOf(
            Arguments.of(0.3, Rating.CAUTION),
            Arguments.of(1.2, null),
            Arguments.of(2.0, Rating.POSITIVE),
        )

        @JvmStatic
        fun roeRatingCases() = listOf(
            Arguments.of(-5.0, Rating.DANGER),
            Arguments.of(5.0, Rating.CAUTION),
            Arguments.of(17.0, null),
            Arguments.of(25.0, Rating.POSITIVE),
        )

        @JvmStatic
        fun profitMarginRatingCases() = listOf(
            Arguments.of(-5.0, Rating.DANGER),
            Arguments.of(3.0, Rating.CAUTION),
            Arguments.of(15.0, null),
            Arguments.of(25.0, Rating.POSITIVE),
        )

        @JvmStatic
        fun earningsEstimateRatingCases() = listOf(
            Arguments.of(-1.0, Rating.DANGER),
            Arguments.of(3.0, Rating.CAUTION),
            Arguments.of(12.0, Rating.POSITIVE),
            Arguments.of(18.0, Rating.CAUTION),
        )

        @JvmStatic
        fun pegRatingCases() = listOf(
            Arguments.of(10.0, null),
            Arguments.of(15.0, null),
            Arguments.of(20.0, null),
            Arguments.of(21.0, Rating.CAUTION),
            Arguments.of(40.0, Rating.CAUTION),
        )

        @JvmStatic
        fun dynamicPaybackRatingCases() = listOf(
            Arguments.of(50.0, 10.0, 10.0, Rating.POSITIVE),
            Arguments.of(150.0, 7.47, 8.65, null),
            Arguments.of(259.37, 7.47, 8.65, Rating.CAUTION),
            Arguments.of(500.0, 7.47, 8.65, Rating.DANGER),
        )
    }
}
