package org.n27.stonks.data.persistence

import org.n27.stonks.domain.model.MacroIndicators
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator

class MacroIndicatorsCache(
    private val prefs: PreferencesStore = SystemPreferencesStore()
) {

    fun save(indicators: MacroIndicators) {
        prefs.putLong(KEY_TIMESTAMP, System.currentTimeMillis())
        prefs.putDouble(KEY_BUND_YIELD_10Y, indicators.bundYield10Y.value)
        prefs.put(KEY_BUND_YIELD_10Y_DATE, indicators.bundYield10Y.date)
        prefs.putDouble(KEY_GERMAN_CPI, indicators.germanCpi.value)
        prefs.put(KEY_GERMAN_CPI_DATE, indicators.germanCpi.date)
        prefs.putDouble(KEY_US_REAL_YIELD_10Y, indicators.usRealYield10Y.value)
        prefs.put(KEY_US_REAL_YIELD_10Y_DATE, indicators.usRealYield10Y.date)
    }

    fun load(): Pair<Long, MacroIndicators>? {
        val timestamp = prefs.getLong(KEY_TIMESTAMP, -1L).takeIf { it != -1L } ?: return null
        val bundYield = prefs.getDouble(KEY_BUND_YIELD_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val bundYieldDate = prefs.get(KEY_BUND_YIELD_10Y_DATE, null) ?: return null
        val germanCpi = prefs.getDouble(KEY_GERMAN_CPI, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val germanCpiDate = prefs.get(KEY_GERMAN_CPI_DATE, null) ?: return null
        val usRealYield = prefs.getDouble(KEY_US_REAL_YIELD_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val usRealYieldDate = prefs.get(KEY_US_REAL_YIELD_10Y_DATE, null) ?: return null
        return timestamp to MacroIndicators(
            bundYield10Y = MacroIndicator(bundYield, bundYieldDate),
            germanCpi = MacroIndicator(germanCpi, germanCpiDate),
            usRealYield10Y = MacroIndicator(usRealYield, usRealYieldDate),
        )
    }

    companion object {
        private const val KEY_TIMESTAMP = "macro_indicators_timestamp"
        private const val KEY_BUND_YIELD_10Y = "macro_european_treasury_10y"
        private const val KEY_BUND_YIELD_10Y_DATE = "macro_european_treasury_10y_date"
        private const val KEY_GERMAN_CPI = "macro_german_cpi"
        private const val KEY_GERMAN_CPI_DATE = "macro_german_cpi_date"
        private const val KEY_US_REAL_YIELD_10Y = "macro_us_real_yield_10y"
        private const val KEY_US_REAL_YIELD_10Y_DATE = "macro_us_real_yield_10y_date"
    }
}
