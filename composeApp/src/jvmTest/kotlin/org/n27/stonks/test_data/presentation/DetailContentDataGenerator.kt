package org.n27.stonks.test_data.presentation

import kotlinx.collections.immutable.toPersistentList
import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.detail.entities.DetailState.Content
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Cell
import stonks.composeapp.generated.resources.*
import kotlin.text.Typography.nbsp

fun getDetailContent(
    symbol: String = "AAPL",
    name: String = "Apple Inc.",
    price: String = "259,37${nbsp}US$",
    lastUpdated: String? = "10. Jan 17:55",
    cells: List<Cell> = listOf(
        Cell(
            title = Res.string.dividend_yield,
            value = "0.40 %",
            description = Res.string.dividend_yield_description,
            delta = null
        ),
        Cell(
            title = Res.string.eps,
            value = "7,47${nbsp}US$",
            description = Res.string.eps_description,
            delta = null
        ),
        Cell(
            title = Res.string.growth,
            value = "86.40 %",
            description = Res.string.growth_description,
            delta = null
        ),
        Cell(
            title = Res.string.forward_growth,
            value = "7.72 %",
            description = Res.string.forward_growth_description,
            delta = null
        ),
        Cell(
            title = Res.string.intrinsic_value,
            value = "93,38${nbsp}US$",
            description = Res.string.intrinsic_value_description,
            delta = DeltaTextEntity(
                value = "165,99${nbsp}US$",
                percentage = "64.00 %",
                state = DeltaState.NEGATIVE
            )
        ),
        Cell(
            title = Res.string.forward_intrinsic_value,
            value = "100,58${nbsp}US$",
            description = Res.string.forward_intrinsic_value_description,
            delta = DeltaTextEntity(
                value = "158,79${nbsp}US$",
                percentage = "61.22 %",
                state = DeltaState.NEGATIVE
            )
        ),
        Cell(
            title = Res.string.pe,
            value = "34.72",
            description = Res.string.pe_description,
            delta = null
        ),
        Cell(
            title = Res.string.pb,
            value = "51.97",
            description = Res.string.pb_description,
            delta = null
        )
    )
) = Content(
    symbol = symbol,
    icon = null,
    name = name,
    price = price,
    lastUpdated = lastUpdated,
    cells = cells.toPersistentList(),
)
