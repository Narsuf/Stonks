package org.n27.stonks.presentation.detail.mapping

import org.junit.Assert.assertEquals
import org.junit.Test
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.presentation.getDetailContent

class DetailMapperTest {

    @Test
    fun `toDetailContent should map Stock to Content correctly`() {
        val result = getStock().toDetailContent()

        assertEquals(getDetailContent(), result)
    }
}
