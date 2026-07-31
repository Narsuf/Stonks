package org.n27.stonks.data.remote.bundesbank.mapping

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator
import org.n27.stonks.test_data.data.getBundesbankResponse
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BundesbankMapperTest {

    @ParameterizedTest(name = "{3}")
    @MethodSource("toDomainCases")
    fun toDomain(
        dates: List<String>,
        observations: Map<String, List<Any?>>,
        expected: MacroIndicator,
        @Suppress("UNUSED_PARAMETER") description: String,
    ) {
        val raw = getBundesbankResponse(dates = dates, observations = observations)

        assertEquals(expected, raw.toDomain())
    }

    @Test
    fun `toDomain should throw when every observation is null`() {
        val raw = getBundesbankResponse(
            dates = listOf("2026-07-24", "2026-07-25"),
            observations = mapOf("0" to listOf(null), "1" to listOf(null)),
        )

        assertFailsWith<IllegalStateException> { raw.toDomain() }
    }

    companion object {

        @JvmStatic
        fun toDomainCases() = listOf(
            Arguments.of(
                listOf("2026-07-24", "2026-07-25", "2026-07-26"),
                mapOf("0" to listOf("3.21"), "1" to listOf("3.19"), "2" to listOf("3.16")),
                MacroIndicator(3.16, "2026-07-26"),
                "returns the value and date at the highest observation index",
            ),
            Arguments.of(
                listOf("2026-07-24", "2026-07-25", "2026-07-26", "2026-07-27"),
                mapOf("0" to listOf("3.21"), "1" to listOf(null), "2" to listOf(null), "3" to listOf(null)),
                MacroIndicator(3.21, "2026-07-24"),
                "skips null observations and returns the latest non-null value",
            ),
            Arguments.of(
                listOf("2026-07-24"),
                mapOf("0" to listOf("3.21")),
                MacroIndicator(3.21, "2026-07-24"),
                "works when a single observation is present",
            ),
            Arguments.of(
                listOf("2026-07-24", "2026-07-25", "2026-07-26"),
                mapOf("2" to listOf("3.16"), "0" to listOf("3.21"), "1" to listOf("3.19")),
                MacroIndicator(3.16, "2026-07-26"),
                "picks the highest index regardless of map insertion order",
            ),
        )
    }
}
