package org.n27.stonks.data.fred

import org.n27.stonks.domain.models.FredYields
import java.util.prefs.Preferences

class FredYieldsCache {

    private val prefs = Preferences.userNodeForPackage(FredYieldsCache::class.java)

    fun save(yields: FredYields) {
        prefs.putLong(KEY_TIMESTAMP, System.currentTimeMillis())
        prefs.putDouble(KEY_TREASURY_10Y, yields.treasury10Y)
        prefs.putDouble(KEY_EUROPEAN_TREASURY_10Y, yields.europeanTreasury10Y)
        prefs.putDouble(KEY_CORPORATE_AAA, yields.corporateAAA)
    }

    fun load(): Pair<Long, FredYields>? {
        val timestamp = prefs.getLong(KEY_TIMESTAMP, -1L).takeIf { it != -1L } ?: return null
        val treasury = prefs.getDouble(KEY_TREASURY_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val europeanTreasury = prefs.getDouble(KEY_EUROPEAN_TREASURY_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val corporate = prefs.getDouble(KEY_CORPORATE_AAA, Double.NaN).takeIf { !it.isNaN() } ?: return null
        return timestamp to FredYields(treasury10Y = treasury, europeanTreasury10Y = europeanTreasury, corporateAAA = corporate)
    }

    companion object {
        private const val KEY_TIMESTAMP = "fred_yields_timestamp"
        private const val KEY_TREASURY_10Y = "fred_treasury_10y"
        private const val KEY_EUROPEAN_TREASURY_10Y = "fred_european_treasury_10y"
        private const val KEY_CORPORATE_AAA = "fred_corporate_aaa"
    }
}
