package org.n27.stonks.test_data.data

import org.n27.stonks.data.model.MacroIndicatorRaw

fun getMacroIndicatorRaw(
    value: Double = 1.5,
    date: String = "2026-01-01",
) = MacroIndicatorRaw(value, date)
