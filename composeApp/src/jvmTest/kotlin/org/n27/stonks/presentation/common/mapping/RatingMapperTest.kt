package org.n27.stonks.presentation.common.mapping

import org.junit.jupiter.api.Test
import org.n27.stonks.domain.models.Rating
import org.n27.stonks.presentation.common.AppColors
import kotlin.test.assertEquals

class RatingMapperTest {

    @Test
    fun `POSITIVE maps to Green`() {
        assertEquals(AppColors.Green, Rating.POSITIVE.toColor())
    }

    @Test
    fun `CAUTION maps to Yellow`() {
        assertEquals(AppColors.Yellow, Rating.CAUTION.toColor())
    }

    @Test
    fun `WARNING maps to Orange`() {
        assertEquals(AppColors.Orange, Rating.WARNING.toColor())
    }

    @Test
    fun `DANGER maps to Red`() {
        assertEquals(AppColors.Red, Rating.DANGER.toColor())
    }
}
