package org.n27.stonks.data.remote.mapping

import org.n27.stonks.data.remote.model.BalanceSheetRaw
import org.n27.stonks.data.remote.model.ComputedRaw
import org.n27.stonks.data.remote.model.DividendsRaw
import org.n27.stonks.data.remote.model.IncomeStatementRaw
import org.n27.stonks.data.remote.model.MetricValueRaw
import org.n27.stonks.data.remote.model.RatingRaw
import org.n27.stonks.data.remote.model.StockRaw
import org.n27.stonks.data.remote.model.StocksRaw
import org.n27.stonks.data.remote.model.ValuationMeasuresRaw
import org.n27.stonks.domain.model.MetricValue
import org.n27.stonks.domain.model.Rating
import org.n27.stonks.domain.model.Stocks
import org.n27.stonks.domain.model.Stocks.Stock.*

internal fun StocksRaw.toDomain() = Stocks(
    items = items.map { it.toDomain() },
    nextPage = nextPage,
)

internal fun StockRaw.toDomain() = Stocks.Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = dividends?.toDomain(),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
    incomeStatement = incomeStatement?.toDomain(),
    earningsEstimate = earningsEstimate?.toDomain(),
    valuationMeasures = valuationMeasures?.toDomain(),
    balanceSheet = balanceSheet?.toDomain(),
    roe = roe?.toDomain(),
    profitMargin = profitMargin?.toDomain(),
    computed = computed?.toDomain(),
)

private fun DividendsRaw.toDomain() = Dividends(
    dividendYield = dividendYield?.toDomain(),
    payoutRatio = payoutRatio?.toDomain(),
)

private fun IncomeStatementRaw.toDomain() = IncomeStatement(
    eps = eps?.toDomain(),
    earningsQuarterlyGrowth = earningsQuarterlyGrowth?.toDomain(),
)

private fun ValuationMeasuresRaw.toDomain() = ValuationMeasures(
    pe = pe?.toDomain(),
    valuationFloor = valuationFloor,
    intrinsicValue = intrinsicValue,
)

private fun BalanceSheetRaw.toDomain() = BalanceSheet(
    de = de?.toDomain(),
    totalCashPerShare = totalCashPerShare?.toDomain(),
)

private fun ComputedRaw.toDomain() = Computed(
    earningsYield = earningsYield,
    peg = peg?.toDomain(),
    dynamicPayback = dynamicPayback?.toDomain(),
)

private fun MetricValueRaw.toDomain() = MetricValue(
    value = value,
    rating = rating?.toDomain(),
    variation = variation,
)

private fun RatingRaw.toDomain() = when (this) {
    RatingRaw.POSITIVE -> Rating.POSITIVE
    RatingRaw.CAUTION -> Rating.CAUTION
    RatingRaw.WARNING -> Rating.WARNING
    RatingRaw.DANGER -> Rating.DANGER
}
