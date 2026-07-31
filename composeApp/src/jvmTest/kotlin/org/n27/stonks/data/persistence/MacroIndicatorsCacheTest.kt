package org.n27.stonks.data.persistence

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.n27.stonks.test_data.domain.getMacroIndicators
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MacroIndicatorsCacheTest {

    private lateinit var prefs: FakePreferencesStore
    private lateinit var cache: MacroIndicatorsCache
    private val indicators = getMacroIndicators()

    @BeforeEach
    fun setUp() {
        prefs = FakePreferencesStore()
        cache = MacroIndicatorsCache(prefs)
    }

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

    private class FakePreferencesStore : PreferencesStore {
        private val longs = mutableMapOf<String, Long>()
        private val doubles = mutableMapOf<String, Double>()
        private val strings = mutableMapOf<String, String>()

        override fun putLong(key: String, value: Long) {
            longs[key] = value
        }

        override fun getLong(key: String, default: Long): Long = longs[key] ?: default

        override fun putDouble(key: String, value: Double) {
            doubles[key] = value
        }

        override fun getDouble(key: String, default: Double): Double = doubles[key] ?: default

        override fun put(key: String, value: String) {
            strings[key] = value
        }

        override fun get(key: String, default: String?): String? = strings[key] ?: default
    }
}
