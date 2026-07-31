package org.n27.stonks.presentation.detail.mapping

import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.StringResource
import org.n27.stonks.domain.model.MacroIndicators
import org.n27.stonks.domain.model.Stocks.Stock
import org.n27.stonks.presentation.common.extensions.*
import org.n27.stonks.presentation.detail.entities.DetailState.Content
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Cell
import org.n27.stonks.presentation.detail.entities.DetailState.Content.Item
import stonks.composeapp.generated.resources.*

internal fun Stock.toDetailContent(indicators: MacroIndicators? = null) = Content(
    symbol = symbol,
    icon = logo?.toImageBitmap(),
    name = companyName.truncateAfterDoubleSpace(),
    price = price?.toPrice(currency),
    lastUpdated = lastUpdated?.toDateString(),
    items = buildList {
        fun addSection(title: StringResource, build: MutableList<Item>.() -> Unit) {
            val items = buildList(build)
            if (items.isEmpty()) return
            add(Item.Header(title))
            addAll(items)
        }

        fun MutableList<Item>.addPair(first: Cell?, second: Cell? = null) {
            first?.let { add(Item.CellPair(it, second)) }
        }

        addSection(Res.string.section_dividends) {
            addPair(
                first = dividends?.dividendYield?.toDividendCell(),
                second = dividends?.payoutRatio?.toPayoutRatioCell(),
            )
        }

        addSection(Res.string.section_valuation) {
            addPair(
                first = incomeStatement?.eps?.toEpsCell(currency),
                second = valuationMeasures?.pe?.toPeCell(),
            )
            addPair(
                first = valuationMeasures?.intrinsicValue?.toIntrinsicValueCell(this@toDetailContent),
                second = computeEyRealYieldSpread(
                    earningsYield = computed?.earningsYield,
                    realBundYield = computeRealBundYield(
                        bundYield = indicators?.bundYield10Y?.value,
                        cpi = indicators?.germanCpi?.value,
                    )
                )?.toEyRealYieldSpreadCell(),
            )
            addPair(
                first = computed?.peg?.toPegCell(),
                second = computed?.dynamicPayback?.toDynamicPaybackCell(),
            )
            addPair(
                first = incomeStatement?.earningsQuarterlyGrowth?.toGrowthCell(),
                second = earningsEstimate?.toEarningsEstimateCell(),
            )
        }

        addSection(Res.string.section_fundamentals) {
            addPair(
                first = roe?.toRoeCell(),
                second = profitMargin?.toProfitMarginCell(),
            )
            addPair(
                first = balanceSheet?.de?.toDeCell(),
                second = balanceSheet?.currentRatio?.toCurrentRatioCell(),
            )
        }

        addSection(Res.string.section_bond_yields) {
            addPair(
                first = indicators?.bundYield10Y?.toGermanBundYieldCell(),
                second = indicators?.germanCpi?.toGermanCpiCell(),
            )
        }
    }.toPersistentList(),
    isWatchlisted = isWatchlisted,
)

private fun computeRealBundYield(bundYield: Double?, cpi: Double?) =
    if (bundYield != null && cpi != null) bundYield - cpi else null

private fun computeEyRealYieldSpread(earningsYield: Double?, realBundYield: Double?) =
    if (earningsYield != null && realBundYield != null) earningsYield - realBundYield else null
