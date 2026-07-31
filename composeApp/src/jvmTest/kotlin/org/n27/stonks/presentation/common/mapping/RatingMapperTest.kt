package org.n27.stonks.presentation.common.mapping

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.n27.stonks.domain.model.Rating
import org.n27.stonks.presentation.common.AppColors
import kotlin.test.assertEquals

class RatingMapperTest {

    @ParameterizedTest(name = "{0} maps to {1}")
    @MethodSource("ratingColorCases")
    fun `rating maps to color`(rating: Rating, expected: Color) {
        assertEquals(expected, rating.toColor())
    }

    companion object {
        @JvmStatic
        fun ratingColorCases() = listOf(
            Arguments.of(Rating.POSITIVE, AppColors.Green),
            Arguments.of(Rating.CAUTION, AppColors.Yellow),
            Arguments.of(Rating.WARNING, AppColors.Orange),
            Arguments.of(Rating.DANGER, AppColors.Red),
        )
    }
}
