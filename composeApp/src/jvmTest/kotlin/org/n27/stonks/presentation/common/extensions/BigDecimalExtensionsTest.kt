package org.n27.stonks.presentation.common.extensions

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BigDecimalExtensionsTest {

    private lateinit var defaultLocale: Locale
    private val amount = BigDecimal("123.45")

    @BeforeEach
    fun setUp() {
        defaultLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)
    }

    @AfterEach
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `toPrice should format using the default locale currency when currency is null`() {
        assertEquals("$123.45", amount.toPrice(null))
    }

    @Test
    fun `toPrice should format using the default locale currency when currency is an empty string`() {
        assertEquals("$123.45", amount.toPrice(""))
    }

    @Test
    fun `toPrice should format using the default locale currency when currency code is invalid`() {
        assertEquals("$123.45", amount.toPrice("not-a-currency"))
    }

    @Test
    fun `toPrice should format using the given currency when it is valid`() {
        val result = amount.toPrice("EUR")

        assertEquals("€123.45", result)
        assertNotEquals(amount.toPrice(null), result)
    }
}
