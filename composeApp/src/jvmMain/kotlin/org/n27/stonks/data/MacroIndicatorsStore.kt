package org.n27.stonks.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.n27.stonks.data.remote.bundesbank.BundesbankApi
import org.n27.stonks.data.remote.bundesbank.mapping.toDomain
import org.n27.stonks.data.remote.fred.FredApi
import org.n27.stonks.data.remote.fred.mapping.toDomain
import org.n27.stonks.data.persistence.MacroIndicatorsCache
import org.n27.stonks.domain.model.MacroIndicators
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class MacroIndicatorsStore(
    private val bundesbankApi: BundesbankApi,
    private val fredApi: FredApi,
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
                val bundYield = async { bundesbankApi.getGermanBundYield10Y() }
                val germanCpi = async { bundesbankApi.getGermanCpiYoY() }
                val usRealYield = async { fredApi.getRealTreasuryYield10Y() }
                MacroIndicators(
                    bundYield10Y = bundYield.await().toDomain(),
                    germanCpi = germanCpi.await().toDomain(),
                    usRealYield10Y = usRealYield.await().toDomain(),
                )
            }
        }.onSuccess { indicators ->
            cache.save(indicators)
            _indicators.emit(indicators)
        }.onFailure { it.printStackTrace() }
    }

    private fun Long.isToday(): Boolean {
        val zone = ZoneId.systemDefault()
        val savedDate = Instant.ofEpochMilli(this).atZone(zone).toLocalDate()
        return savedDate.isEqual(LocalDate.now(zone))
    }
}
