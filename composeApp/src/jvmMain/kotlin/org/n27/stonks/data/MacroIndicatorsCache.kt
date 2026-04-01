package org.n27.stonks.data

import org.n27.stonks.domain.models.MacroIndicators
import org.n27.stonks.domain.models.MacroIndicators.MacroIndicator
import java.util.prefs.Preferences

class MacroIndicatorsCache {

    private val prefs = Preferences.userNodeForPackage(MacroIndicatorsCache::class.java)

    fun save(indicators: MacroIndicators) {
        prefs.putLong(KEY_TIMESTAMP, System.currentTimeMillis())
        prefs.putDouble(KEY_TREASURY_10Y, indicators.treasury10Y.value)
        prefs.put(KEY_TREASURY_10Y_DATE, indicators.treasury10Y.date)
        prefs.putDouble(KEY_EUROPEAN_TREASURY_10Y, indicators.europeanTreasury10Y.value)
        prefs.put(KEY_EUROPEAN_TREASURY_10Y_DATE, indicators.europeanTreasury10Y.date)
        prefs.putDouble(KEY_CORPORATE_AAA, indicators.corporateAAA.value)
        prefs.put(KEY_CORPORATE_AAA_DATE, indicators.corporateAAA.date)
        prefs.putDouble(KEY_GERMAN_CPI, indicators.germanCpi.value)
        prefs.put(KEY_GERMAN_CPI_DATE, indicators.germanCpi.date)
    }

    fun load(): Pair<Long, MacroIndicators>? {
        val timestamp = prefs.getLong(KEY_TIMESTAMP, -1L).takeIf { it != -1L } ?: return null
        val treasury = prefs.getDouble(KEY_TREASURY_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val treasuryDate = prefs.get(KEY_TREASURY_10Y_DATE, null) ?: return null
        val europeanTreasury = prefs.getDouble(KEY_EUROPEAN_TREASURY_10Y, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val europeanTreasuryDate = prefs.get(KEY_EUROPEAN_TREASURY_10Y_DATE, null) ?: return null
        val corporate = prefs.getDouble(KEY_CORPORATE_AAA, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val corporateDate = prefs.get(KEY_CORPORATE_AAA_DATE, null) ?: return null
        val germanCpi = prefs.getDouble(KEY_GERMAN_CPI, Double.NaN).takeIf { !it.isNaN() } ?: return null
        val germanCpiDate = prefs.get(KEY_GERMAN_CPI_DATE, null) ?: return null
        return timestamp to MacroIndicators(
            treasury10Y = MacroIndicator(treasury, treasuryDate),
            europeanTreasury10Y = MacroIndicator(europeanTreasury, europeanTreasuryDate),
            corporateAAA = MacroIndicator(corporate, corporateDate),
            germanCpi = MacroIndicator(germanCpi, germanCpiDate),
        )
    }

    companion object {
        private const val KEY_TIMESTAMP = "macro_indicators_timestamp"
        private const val KEY_TREASURY_10Y = "macro_treasury_10y"
        private const val KEY_TREASURY_10Y_DATE = "macro_treasury_10y_date"
        private const val KEY_EUROPEAN_TREASURY_10Y = "macro_european_treasury_10y"
        private const val KEY_EUROPEAN_TREASURY_10Y_DATE = "macro_european_treasury_10y_date"
        private const val KEY_CORPORATE_AAA = "macro_corporate_aaa"
        private const val KEY_CORPORATE_AAA_DATE = "macro_corporate_aaa_date"
        private const val KEY_GERMAN_CPI = "macro_german_cpi"
        private const val KEY_GERMAN_CPI_DATE = "macro_german_cpi_date"
    }
}
