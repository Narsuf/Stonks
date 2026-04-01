package org.n27.stonks.test_data.data

import org.n27.stonks.data.models.MacroIndicatorRaw

fun getMacroIndicatorRaw(
    value: Double = 4.5,
    date: String = "2024-01-01",
) = MacroIndicatorRaw(value, date)
