package org.n27.stonks.presentation.detail.mapping

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.n27.stonks.test_data.domain.getFredYields
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.presentation.getDetailContent
import java.util.*
import kotlin.test.assertEquals

class DetailMapperTest {

    @BeforeEach
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
