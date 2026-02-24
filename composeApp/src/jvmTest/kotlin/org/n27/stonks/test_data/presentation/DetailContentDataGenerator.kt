package org.n27.stonks.test_data.presentation

import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.presentation.common.composables.DeltaState
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.detail.entities.DetailState.Content
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Cell
import stonks.composeapp.generated.resources.*

fun getDetailContent(
    symbol: String = "AAPL",
    name: String = "Apple Inc.",
    price: String = "$259.37",
    lastUpdated: String? = "10. Jan 16:55",
    cells: List<Cell> = listOf(
        getDetailContentCell(
            title = Res.string.dividend_yield,
            value = "0.40 %",
            description = Res.string.dividend_yield_description
        ),
        getDetailContentCell(
            title = Res.string.payout_ratio,
            value = "13.89 %",
            description = Res.string.payout_ratio_description
        ),
        getDetailContentCell(
            title = Res.string.growth,
            value = "86.40 %",
            description = Res.string.growth_description
        ),
        getDetailContentCell(
            title = Res.string.forward_growth,
            value = "7.72 %",
            description = Res.string.forward_growth_description
        ),
        getDetailContentCell(
            title = Res.string.intrinsic_value,
            value = "$93.38",
            description = Res.string.intrinsic_value_description,
            delta = DeltaTextEntity(
                value = "$165.99",
                percentage = "64.00 %",
                state = DeltaState.NEGATIVE
            )
        ),
        getDetailContentCell(
            title = Res.string.forward_intrinsic_value,
            value = "$100.58",
            description = Res.string.forward_intrinsic_value_description,
            delta = DeltaTextEntity(
                value = "$158.79",
                percentage = "61.22 %",
                state = DeltaState.NEGATIVE
            )
        ),
        getDetailContentCell(
            title = Res.string.pe,
            value = "34.72",
            description = Res.string.pe_description
        ),
        getDetailContentCell(
            title = Res.string.pb,
            value = "51.97",
            description = Res.string.pb_description
        ),
        getDetailContentCell(
            title = Res.string.eps,
            value = "$7.47",
            description = Res.string.eps_description
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

fun getDetailContentCell(
    title: StringResource = Res.string.dividend_yield,
    value: String = "0.40 %",
    description: StringResource = Res.string.dividend_yield_description,
    delta: DeltaTextEntity? = null
) = Cell(
    title = title,
    value = value,
    description = description,
    delta = delta
)
