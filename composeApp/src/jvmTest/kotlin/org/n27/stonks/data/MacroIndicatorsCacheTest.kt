package org.n27.stonks.data

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.n27.stonks.test_data.domain.getMacroIndicators
import java.util.prefs.Preferences
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MacroIndicatorsCacheTest {

    private val prefs = Preferences.userNodeForPackage(MacroIndicatorsCache::class.java)
    private val cache = MacroIndicatorsCache()
    private val indicators = getMacroIndicators()

    @BeforeEach
    fun setUp() = prefs.clear()

    @AfterEach
    fun tearDown() = prefs.clear()

    @Test
    fun `load should return null when nothing is saved`() {
        assertNull(cache.load())
    }

    @Test
    fun `load should return saved indicators after save`() {
        val beforeSave = System.currentTimeMillis()
        cache.save(indicators)
        val afterSave = System.currentTimeMillis()

        val (timestamp, loaded) = cache.load()!!

        assertEquals(indicators, loaded)
        assert(timestamp in beforeSave..afterSave)
    }
}
