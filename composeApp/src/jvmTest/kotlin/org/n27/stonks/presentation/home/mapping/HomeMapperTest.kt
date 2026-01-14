package org.n27.stonks.presentation.home.mapping

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg.Resource
import org.n27.stonks.presentation.common.entities.StringResourceWithArgs.Arg.Text
import org.n27.stonks.test_data.domain.getStock
import org.n27.stonks.test_data.domain.getStocks
import org.n27.stonks.test_data.presentation.getHomeContent
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.default_value
import stonks.composeapp.generated.resources.not_set
import stonks.composeapp.generated.resources.valuation_and_growth
import java.util.*

class HomeMapperTest {

    @Before
    fun setup() {
        Locale.setDefault(Locale.US)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun `toContent should map Stocks to Content correctly`() = runTest {
        val result = getStocks().toContent()

        assertEquals(getHomeContent(), result)
    }

    @Test
    fun `toContent should set isEndReached to true when nextPage is null`() = runTest {
        val result = getStocks(nextPage = null).toContent()

        assertEquals(true, result.isEndReached)
    }

    @Test
    fun `Stock toPresentationEntity should show Default when valuationFloor is null`() = runTest {
        val stock = getStock(valuationFloor = null)
        val expected = StringResourceWithArgs(
            resource = Res.string.valuation_and_growth,
            args = persistentListOf(
                Resource(Res.string.default_value),
                Text("7.72 %"),
            )
        )

        val result = listOf(stock).toPresentationEntity()[0]

        assertEquals(expected, result.extraValue)
    }

    @Test
    fun `Stock toPresentationEntity should show Not set when expectedEpsGrowth is null`() = runTest {
        val stock = getStock(expectedEpsGrowth = null)
        val expected = StringResourceWithArgs(
            resource = Res.string.valuation_and_growth,
            args = persistentListOf(
                Text("12.50"),
                Resource(Res.string.not_set),
            )
        )

        val result = listOf(stock).toPresentationEntity()[0]

        assertEquals(expected, result.extraValue)
    }
}

