package org.n27.stonks.data.fred

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.n27.stonks.domain.models.FredYields
import java.util.prefs.Preferences

class FredYieldsCacheTest {

    private val prefs = Preferences.userNodeForPackage(FredYieldsCache::class.java)
    private val cache = FredYieldsCache()
    private val fredYields = FredYields(treasury10Y = 4.5, corporateAAA = 5.2)

    @Before
    fun setUp() = prefs.clear()

    @After
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
