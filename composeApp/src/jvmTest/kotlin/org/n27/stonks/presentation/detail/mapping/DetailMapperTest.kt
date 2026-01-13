package org.n27.stonks.presentation.detail.mapping

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.presentation.getDetailContent
import java.util.*

class DetailMapperTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun `toDetailContent should map Stock to Content correctly`() {
        val result = getStock().toDetailContent()

        assertEquals(getDetailContent(), result)
    }
}
