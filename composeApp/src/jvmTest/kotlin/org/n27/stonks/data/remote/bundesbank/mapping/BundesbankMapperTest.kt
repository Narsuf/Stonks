package org.n27.stonks.data.remote.bundesbank.mapping

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import org.junit.jupiter.api.Test
import org.n27.stonks.data.remote.bundesbank.model.BundesbankResponse
import org.n27.stonks.data.remote.model.MacroIndicatorRaw
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BundesbankMapperTest {

    private fun response(dates: List<String>, observations: Map<String, List<Any?>>): BundesbankResponse {
        val jsonObservations = observations.mapValues { (_, values) ->
            values.map { if (it == null) JsonNull else JsonPrimitive(it.toString()) }
        }
        return BundesbankResponse(
            data = BundesbankResponse.Data(
                structure = BundesbankResponse.Structure(
                    dimensions = BundesbankResponse.Dimensions(
                        observation = listOf(
                            BundesbankResponse.Observation(dates.map { BundesbankResponse.Value(it) })
                        )
                    )
                ),
                dataSets = listOf(
                    BundesbankResponse.DataSet(
                        series = mapOf("0:0:0" to BundesbankResponse.Series(jsonObservations))
                    )
                ),
            )
        )
    }

    @Test
    fun `toRaw should return the value and date at the highest observation index`() {
        val raw = response(
            dates = listOf("2026-07-24", "2026-07-25", "2026-07-26"),
            observations = mapOf("0" to listOf("3.21"), "1" to listOf("3.19"), "2" to listOf("3.16")),
        )

        assertEquals(MacroIndicatorRaw(3.16, "2026-07-26"), raw.toRaw())
    }

    @Test
    fun `toRaw should skip null observations and return the latest non-null value`() {
        val raw = response(
            dates = listOf("2026-07-24", "2026-07-25", "2026-07-26", "2026-07-27"),
            observations = mapOf("0" to listOf("3.21"), "1" to listOf(null), "2" to listOf(null), "3" to listOf(null)),
        )

        assertEquals(MacroIndicatorRaw(3.21, "2026-07-24"), raw.toRaw())
    }

    @Test
    fun `toRaw should throw when every observation is null`() {
        val raw = response(
            dates = listOf("2026-07-24", "2026-07-25"),
            observations = mapOf("0" to listOf(null), "1" to listOf(null)),
        )

        assertFailsWith<IllegalStateException> { raw.toRaw() }
    }

    @Test
    fun `toRaw should work when a single observation is present`() {
        val raw = response(
            dates = listOf("2026-07-24"),
            observations = mapOf("0" to listOf("3.21")),
        )

        assertEquals(MacroIndicatorRaw(3.21, "2026-07-24"), raw.toRaw())
    }

    @Test
    fun `toRaw should pick the highest index regardless of map insertion order`() {
        val raw = response(
            dates = listOf("2026-07-24", "2026-07-25", "2026-07-26"),
            observations = mapOf("2" to listOf("3.16"), "0" to listOf("3.21"), "1" to listOf("3.19")),
        )

        assertEquals(MacroIndicatorRaw(3.16, "2026-07-26"), raw.toRaw())
    }
}
