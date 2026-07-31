package org.n27.stonks.data.remote.bundesbank.mapping

import org.junit.jupiter.api.Test
import org.n27.stonks.domain.model.MacroIndicators.MacroIndicator
import org.n27.stonks.test_data.data.getBundesbankResponse
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BundesbankMapperTest {

    @Test
    fun `toDomain should return the value and date at the highest observation index`() {
        val raw = getBundesbankResponse(
            dates = listOf("2026-07-24", "2026-07-25", "2026-07-26"),
            observations = mapOf("0" to listOf("3.21"), "1" to listOf("3.19"), "2" to listOf("3.16")),
        )

        assertEquals(MacroIndicator(3.16, "2026-07-26"), raw.toDomain())
    }

    @Test
    fun `toDomain should skip null observations and return the latest non-null value`() {
        val raw = getBundesbankResponse(
            dates = listOf("2026-07-24", "2026-07-25", "2026-07-26", "2026-07-27"),
            observations = mapOf("0" to listOf("3.21"), "1" to listOf(null), "2" to listOf(null), "3" to listOf(null)),
        )

        assertEquals(MacroIndicator(3.21, "2026-07-24"), raw.toDomain())
    }

    @Test
    fun `toDomain should throw when every observation is null`() {
        val raw = getBundesbankResponse(
            dates = listOf("2026-07-24", "2026-07-25"),
            observations = mapOf("0" to listOf(null), "1" to listOf(null)),
        )

        assertFailsWith<IllegalStateException> { raw.toDomain() }
    }

    @Test
    fun `toDomain should work when a single observation is present`() {
        val raw = getBundesbankResponse(
            dates = listOf("2026-07-24"),
            observations = mapOf("0" to listOf("3.21")),
        )

        assertEquals(MacroIndicator(3.21, "2026-07-24"), raw.toDomain())
    }

    @Test
    fun `toDomain should pick the highest index regardless of map insertion order`() {
        val raw = getBundesbankResponse(
            dates = listOf("2026-07-24", "2026-07-25", "2026-07-26"),
            observations = mapOf("2" to listOf("3.16"), "0" to listOf("3.21"), "1" to listOf("3.19")),
        )

        assertEquals(MacroIndicator(3.16, "2026-07-26"), raw.toDomain())
    }
}
