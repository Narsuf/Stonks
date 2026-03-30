package org.n27.stonks.data.fred

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.n27.stonks.domain.models.FredYields
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class FredYieldsStore(
    private val fredApi: FredApi,
    private val cache: FredYieldsCache,
) {

    private val _yields = MutableStateFlow<FredYields?>(null)
    val yields = _yields.asStateFlow()

    suspend fun refresh() {
        cache.load()
            ?.takeIf { (savedAt, _) -> savedAt.isToday() }
            ?.let { (_, cachedYields) ->
                _yields.emit(cachedYields)
                return
            }

        runCatching {
            coroutineScope {
                val treasury = async { fredApi.getTreasuryYield10Y() }
                val corporate = async { fredApi.getCorporateBondYieldAAA() }
                FredYields(treasury10Y = treasury.await(), corporateAAA = corporate.await())
            }
        }.onSuccess { yields ->
            cache.save(yields)
            _yields.emit(yields)
        }
    }

    private fun Long.isToday(): Boolean {
        val zone = ZoneId.systemDefault()
        val savedDate = Instant.ofEpochMilli(this).atZone(zone).toLocalDate()
        return savedDate.isEqual(LocalDate.now(zone))
    }

}
