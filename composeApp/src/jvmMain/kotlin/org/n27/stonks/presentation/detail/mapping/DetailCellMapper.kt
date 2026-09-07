package org.n27.stonks.presentation.detail.mapping

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator
import org.n27.stonks.domain.model.MetricValue
import org.n27.stonks.domain.model.Stocks.Stock
import org.n27.stonks.presentation.common.AppColors
import org.n27.stonks.presentation.common.composables.DeltaTextEntity
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.common.mapping.toColor
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Cell
import stonks.composeapp.generated.resources.*

internal fun Double.toEyRealYieldSpreadCell() = toFormattedPercentage().toCell(
    title = Res.string.ey_real_yield_spread,
    description = StringResourceWithArgs(Res.string.ey_real_yield_spread_description),
    color = takeIf { it < 0 }?.let { AppColors.Orange },
)

internal fun MacroIndicator.toGermanBundYieldCell() = value.toFormattedPercentage().toCell(
    title = Res.string.bund_yield_10y,
    description = StringResourceWithArgs(Res.string.bund_yield_10y_description, persistentListOf(Arg.Text(date.toFormattedDate()))),
)

internal fun MacroIndicator.toGermanCpiCell() = value.toFormattedPercentage().toCell(
    title = Res.string.german_cpi,
    description = StringResourceWithArgs(Res.string.german_cpi_description, persistentListOf(Arg.Text(date.toFormattedDate()))),
)

internal fun MetricValue.toDividendCell() = value.toFormattedPercentage().toCell(
    title = Res.string.dividend_yield,
    description = StringResourceWithArgs(Res.string.dividend_yield_description),
    delta = value.getVariationDelta(variation) { it.toFormattedString() },
)

internal fun MetricValue.toPayoutRatioCell() = value.toFormattedPercentage().toCell(
    title = Res.string.payout_ratio,
    description = StringResourceWithArgs(Res.string.payout_ratio_description),
    color = rating?.toColor(),
)

internal fun Double.toIntrinsicValueCell(stock: Stock) = toPrice(stock.currency)?.toCell(
    title = Res.string.intrinsic_value,
    description = StringResourceWithArgs(Res.string.intrinsic_value_description),
    delta = stock.price?.getTargetPrice(this, stock.currency),
)

internal fun MetricValue.toDynamicPaybackCell() = value.toFormattedString().toCell(
    title = Res.string.dynamic_payback,
    description = StringResourceWithArgs(Res.string.dynamic_payback_description),
    color = rating?.toColor(),
)

internal fun MetricValue.toPeCell() = value.toFormattedString().toCell(
    title = Res.string.pe,
    description = StringResourceWithArgs(Res.string.pe_description),
    color = rating?.toColor(),
    delta = value.getVariationDelta(variation) { it.toFormattedString() },
)

internal fun MetricValue.toPegCell() = value.toFormattedString().toCell(
    title = Res.string.peg,
    description = StringResourceWithArgs(Res.string.peg_description),
    color = rating?.toColor(),
)

internal fun MetricValue.toGrowthCell() = value.toFormattedPercentage().toCell(
    title = Res.string.growth,
    description = StringResourceWithArgs(Res.string.growth_description),
    delta = value.getVariationDelta(variation) { it.toFormattedString() },
)

internal fun MetricValue.toEpsCell(currency: String?) = value.toPrice(currency)?.toCell(
    title = Res.string.eps,
    description = StringResourceWithArgs(Res.string.eps_description),
    delta = value.getVariationDelta(variation) { it.toPrice(currency) },
)

internal fun MetricValue.toProfitMarginCell() = value.toFormattedPercentage().toCell(
    title = Res.string.profit_margin,
    description = StringResourceWithArgs(Res.string.profit_margin_description),
    color = rating?.toColor(),
    delta = value.getVariationDelta(variation) { it.toFormattedString() },
)

internal fun MetricValue.toRoeCell() = value.toFormattedPercentage().toCell(
    title = Res.string.roe,
    description = StringResourceWithArgs(Res.string.roe_description),
    color = rating?.toColor(),
    delta = value.getVariationDelta(variation) { it.toFormattedString() },
)

internal fun MetricValue.toDeCell() = value.toFormattedString().toCell(
    title = Res.string.de,
    description = StringResourceWithArgs(Res.string.de_description),
    color = rating?.toColor(),
    delta = value.getVariationDelta(variation) { it.toFormattedString() },
)

internal fun MetricValue.toEarningsEstimateCell() = value.toFormattedPercentage().toCell(
    title = Res.string.earnings_estimate,
    description = StringResourceWithArgs(Res.string.earnings_estimate_description),
    color = rating?.toColor(),
    delta = value.getVariationDelta(variation) { it.toFormattedString() },
)

internal fun MetricValue.toTotalCashPerShareCell(currency: String?) = value.toPrice(currency)?.toCell(
    title = Res.string.total_cash_per_share,
    description = StringResourceWithArgs(Res.string.total_cash_per_share_description),
    delta = value.getVariationDelta(variation) { it.toPrice(currency) },
)

private fun String.toCell(
    title: StringResource,
    description: StringResourceWithArgs,
    delta: DeltaTextEntity? = null,
    color: Color? = null,
) = Cell(
    title = title,
    value = this,
    description = description,
    delta = delta,
    color = color,
)
