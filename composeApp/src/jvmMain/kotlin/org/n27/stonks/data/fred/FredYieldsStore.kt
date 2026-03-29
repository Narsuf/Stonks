package org.n27.stonks.data.fred

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.n27.stonks.domain.models.FredYields

class FredYieldsStore(
    private val fredApi: FredApi,
    private val cache: FredYieldsCache,
) {

    private val _yields = MutableStateFlow<FredYields?>(null)
    val yields = _yields.asStateFlow()

    suspend fun refresh() {
        cache.loadIfToday()?.let {
            _yields.emit(it)
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
}
