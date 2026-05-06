package org.n27.stonks.data.eurostat.mapping

import org.n27.stonks.data.eurostat.model.EurostatResponse
import org.n27.stonks.data.model.MacroIndicatorRaw

internal fun EurostatResponse.toRaw(): MacroIndicatorRaw {
    val maxIndex = value.keys.maxOf { it.toInt() }
    val value = value.getValue(maxIndex.toString())
    val date = dimension.time.category.index.entries.first { it.value == maxIndex }.key
    return MacroIndicatorRaw(value, date)
}
