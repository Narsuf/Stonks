package org.n27.stonks.data.remote.eurostat.mapping

import org.n27.stonks.data.remote.eurostat.model.EurostatResponse
import org.n27.stonks.data.remote.model.MacroIndicatorRaw

internal fun EurostatResponse.toRaw(): MacroIndicatorRaw {
    val maxIndex = value.keys.maxOf { it.toInt() }
    val value = value.getValue(maxIndex.toString())
    val date = dimension.time.category.index.entries.first { it.value == maxIndex }.key
    return MacroIndicatorRaw(value, date)
}
