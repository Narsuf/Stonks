package org.n27.stonks.presentation.common

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppColors {

    private val baseColors = lightColorScheme()

    val customColorScheme: ColorScheme = baseColors.copy(
        primary = Color(0xFF4EA6AA),
    )
}
