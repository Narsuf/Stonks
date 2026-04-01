package org.n27.stonks.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.n27.stonks.data.eurostat.EurostatApi
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
                val treasury = treasury.await()
                val europeanTreasury = europeanTreasury.await()
                val corporateAAA = corporate.await()
                val germanCpiResult = germanCpi.await()
                MacroIndicators(
                    treasury10Y = treasury.value,
                    treasury10YDate = treasury.date,
                    europeanTreasury10Y = europeanTreasury.value,
                    europeanTreasury10YDate = europeanTreasury.date,
                    corporateAAA = corporateAAA.value,
                    germanCpi = germanCpiResult?.value,
                    germanCpiDate = germanCpiResult?.date,
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
