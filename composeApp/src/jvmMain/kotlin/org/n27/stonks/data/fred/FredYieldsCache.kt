package org.n27.stonks.data.fred

import org.n27.stonks.domain.models.FredYields
import java.time.LocalDate
import java.util.prefs.Preferences

class FredYieldsCache {

    private val prefs = Preferences.userNodeForPackage(FredYieldsCache::class.java)

    fun save(yields: FredYields) {
        prefs.put(KEY_DATE, LocalDate.now().toString())
        prefs.putDouble(KEY_TREASURY_10Y, yields.treasury10Y)
        prefs.putDouble(KEY_CORPORATE_AAA, yields.corporateAAA)
    }

    fun loadIfToday(): FredYields? {
        val savedDate = prefs.get(KEY_DATE, null) ?: return null
        if (savedDate != LocalDate.now().toString()) return null
        val treasury = prefs.getDouble(KEY_TREASURY_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val corporate = prefs.getDouble(KEY_CORPORATE_AAA, Double.NaN).takeIf { !it.isNaN() } ?: return null
        return FredYields(treasury10Y = treasury, corporateAAA = corporate)
    }

    companion object {
        private const val KEY_DATE = "fred_yields_date"
        private const val KEY_TREASURY_10Y = "fred_treasury_10y"
        private const val KEY_CORPORATE_AAA = "fred_corporate_aaa"
    }
}
