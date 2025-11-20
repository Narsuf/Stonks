package org.n27.stonks.presentation.common.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency

internal fun BigDecimal.toPrice(currency: String?): String? {
    val safeCurrency = runCatching {
        currency?.uppercase()?.let { Currency.getInstance(it) }
    }.getOrNull()

    val format = NumberFormat.getCurrencyInstance()
    safeCurrency?.let { format.currency = it }

    return format.format(this)
}
