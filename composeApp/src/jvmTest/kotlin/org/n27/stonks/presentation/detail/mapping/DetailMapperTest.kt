package org.n27.stonks.presentation.detail.mapping

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Item
import org.n27.stonks.test_data.domain.getFredYields
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.presentation.getDetailContent
import org.n27.stonks.test_data.presentation.getDetailContentCell
import stonks.composeapp.generated.resources.*
import java.util.*

class DetailMapperTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun `toDetailContent should map Stock to Content correctly`() {
        val result = getStock().toDetailContent(fredYields = getFredYields())

        assertEquals(getDetailContent(), result)
    }

    @Test
    fun `toDetailContent should map Stock to Content when all optional fields are null`() {
        val result = getStock(
            dividends = null,
            incomeStatement = null,
            valuationMeasures = null,
            computed = null,
            roe = null,
            profitMargin = null,
            balanceSheet = null,
            earningsEstimate = null,
        ).toDetailContent()

        assertEquals(getDetailContent(items = emptyList()), result)
    }
}
