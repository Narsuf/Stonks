package org.n27.stonks.data

import org.n27.stonks.domain.models.MacroIndicators
import java.util.prefs.Preferences

class MacroIndicatorsCache {

    private val prefs = Preferences.userNodeForPackage(MacroIndicatorsCache::class.java)

    fun save(indicators: MacroIndicators) {
        prefs.putLong(KEY_TIMESTAMP, System.currentTimeMillis())
        prefs.putDouble(KEY_TREASURY_10Y, indicators.treasury10Y)
        indicators.treasury10YDate?.let { prefs.put(KEY_TREASURY_10Y_DATE, it) }
        prefs.putDouble(KEY_EUROPEAN_TREASURY_10Y, indicators.europeanTreasury10Y)
        indicators.europeanTreasury10YDate?.let { prefs.put(KEY_EUROPEAN_TREASURY_10Y_DATE, it) }
        prefs.putDouble(KEY_CORPORATE_AAA, indicators.corporateAAA)
        indicators.germanCpi?.let { prefs.putDouble(KEY_GERMAN_CPI, it) }
        indicators.germanCpiDate?.let { prefs.put(KEY_GERMAN_CPI_DATE, it) }
    }

    fun load(): Pair<Long, MacroIndicators>? {
        val timestamp = prefs.getLong(KEY_TIMESTAMP, -1L).takeIf { it != -1L } ?: return null
        val treasury = prefs.getDouble(KEY_TREASURY_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val europeanTreasury = prefs.getDouble(KEY_EUROPEAN_TREASURY_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val corporate = prefs.getDouble(KEY_CORPORATE_AAA, Double.NaN).takeIf { !it.isNaN() } ?: return null
        return timestamp to MacroIndicators(
            treasury10Y = treasury,
            treasury10YDate = prefs.get(KEY_TREASURY_10Y_DATE, null),
            europeanTreasury10Y = europeanTreasury,
            europeanTreasury10YDate = prefs.get(KEY_EUROPEAN_TREASURY_10Y_DATE, null),
            corporateAAA = corporate,
            germanCpi = prefs.getDouble(KEY_GERMAN_CPI, Double.NaN).takeIf { !it.isNaN() },
            germanCpiDate = prefs.get(KEY_GERMAN_CPI_DATE, null),
        )
    }

    companion object {
        private const val KEY_TIMESTAMP = "macro_indicators_timestamp"
        private const val KEY_TREASURY_10Y = "macro_treasury_10y"
        private const val KEY_TREASURY_10Y_DATE = "macro_treasury_10y_date"
        private const val KEY_EUROPEAN_TREASURY_10Y = "macro_european_treasury_10y"
        private const val KEY_EUROPEAN_TREASURY_10Y_DATE = "macro_european_treasury_10y_date"
        private const val KEY_CORPORATE_AAA = "macro_corporate_aaa"
        private const val KEY_GERMAN_CPI = "macro_german_cpi"
        private const val KEY_GERMAN_CPI_DATE = "macro_german_cpi_date"
    }
}
