package org.n27.stonks.data.json

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonReaderTest {

    @Test
    fun `getSymbols should return a list of symbols from test resources`() = runTest {
        val symbols = JsonReader.getSymbols()
        assertEquals(5, symbols.size)
        assertTrue(symbols.contains("MSFT"))
        assertTrue(symbols.contains("ADS.DE"))
        assertTrue(symbols.contains("6758.T"))
        assertTrue(symbols.contains("SAN"))
        assertTrue(symbols.contains("TSLA"))
    }

    @Test
    fun `getSymbols should not contain duplicates`() = runTest {
        val symbols = JsonReader.getSymbols()
        assertEquals(5, symbols.size)
    }
}
