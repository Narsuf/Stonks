package org.n27.stonks.presentation.common.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import java.math.BigDecimal

@Composable
fun NumberInput(
    value: BigDecimal,
    onValueChange: (BigDecimal) -> Unit,
    maxLength: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(value.toPlainString())) }

    val numberRegex = remember { Regex("^\\d*(\\.\\d{0,2})?$") }

    fun normalizeIntegerPart(integerPart: String): Pair<String, Int> {
        val leadingZeros = integerPart.takeWhile { it == '0' }.length
        val normalized = integerPart.trimStart('0').ifEmpty { "0" }
        val offset = if (normalized == "0" && integerPart.length > 1)
            leadingZeros - 1
        else
            leadingZeros

        return normalized to offset
    }

    LaunchedEffect(value) {
        val formatted = value.toPlainString()
        if (formatted != textFieldValue.text)
            textFieldValue = TextFieldValue(formatted, TextRange(formatted.length))
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            var text = newValue.text
            var cursorPos = newValue.selection.end

            if (!text.matches(Regex("^[0-9.]*$"))) return@OutlinedTextField

            val currentText = textFieldValue.text

            when {
                text.isEmpty() -> {
                    text = "0"
                    cursorPos = 1
                }
                currentText == "0" && text.matches(Regex("[1-9]")) -> cursorPos = 1
                currentText == "0" && text == "." -> {
                    text = "0."
                    cursorPos = 2
                }
            }

            val parts = text.split('.', limit = 2)
            var integerPart = parts.getOrNull(0) ?: ""
            val decimalPart = parts.getOrNull(1) ?: ""

            if (integerPart.length > 1) {
                val (normalizedInt, zeroOffset) = normalizeIntegerPart(integerPart)
                integerPart = normalizedInt
                if (zeroOffset > 0 && cursorPos > 0) cursorPos -= zeroOffset
            }

            text = when {
                text.endsWith(".") -> "$integerPart."
                decimalPart.isNotEmpty() -> "$integerPart.$decimalPart"
                else -> integerPart
            }

            val finalDecimalPart = text.split('.', limit = 2).getOrNull(1) ?: ""
            val isValid = text.matches(numberRegex)
                    && integerPart.length <= maxLength
                    && finalDecimalPart.length <= 2

            if (isValid) {
                val safeCursor = cursorPos.coerceIn(0, text.length)
                textFieldValue = TextFieldValue(text, TextRange(safeCursor))
                onValueChange(text.toBigDecimalOrNull() ?: BigDecimal.ZERO)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        singleLine = true,
    )
}
