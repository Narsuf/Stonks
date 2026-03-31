package org.n27.stonks.data.fred

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.n27.stonks.domain.models.FredYields
import java.util.prefs.Preferences
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FredYieldsCacheTest {

    private val prefs = Preferences.userNodeForPackage(FredYieldsCache::class.java)
    private val cache = FredYieldsCache()
    private val fredYields = FredYields(treasury10Y = 4.5, europeanTreasury10Y = 3.1, corporateAAA = 5.2)

    @BeforeEach
    fun setUp() = prefs.clear()

    @AfterEach
    fun tearDown() = prefs.clear()

    @Test
    fun `load should return null when nothing is saved`() {
        assertNull(cache.load())
    }

    @Test
    fun `load should return saved yields after save`() {
        val beforeSave = System.currentTimeMillis()
        cache.save(fredYields)
        val afterSave = System.currentTimeMillis()

        val (timestamp, yields) = cache.load()!!

        assertEquals(fredYields, yields)
        assert(timestamp in beforeSave..afterSave)
    }
}
