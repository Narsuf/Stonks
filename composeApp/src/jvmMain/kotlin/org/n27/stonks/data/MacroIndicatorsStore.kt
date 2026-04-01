package org.n27.stonks.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.n27.stonks.domain.models.MacroIndicators
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class MacroIndicatorsStore(
    private val fredApi: FredApi,
    private val eurostatApi: EurostatApi,
    private val cache: MacroIndicatorsCache,
) {

    private val _indicators = MutableStateFlow<MacroIndicators?>(null)
    val indicators = _indicators.asStateFlow()

    suspend fun refresh() {
        cache.load()
            ?.takeIf { (savedAt, _) -> savedAt.isToday() }
            ?.let { (_, cached) ->
                _indicators.emit(cached)
                return
            }

        runCatching {
            coroutineScope {
                val treasury = async { fredApi.getTreasuryYield10Y() }
                val europeanTreasury = async { fredApi.getEuropeanTreasuryYield10Y() }
                val corporate = async { fredApi.getCorporateBondYieldAAA() }
                val germanCpi = async { runCatching { eurostatApi.getGermanCpiYoY() }.getOrNull() }
                MacroIndicators(
                    treasury10Y = treasury.await(),
                    europeanTreasury10Y = europeanTreasury.await(),
                    corporateAAA = corporate.await(),
                    germanCpi = germanCpi.await(),
                )
            }
        }.onSuccess { indicators ->
            cache.save(indicators)
            _indicators.emit(indicators)
        }
    }

    private fun Long.isToday(): Boolean {
        val zone = ZoneId.systemDefault()
        val savedDate = Instant.ofEpochMilli(this).atZone(zone).toLocalDate()
        return savedDate.isEqual(LocalDate.now(zone))
    }

}
