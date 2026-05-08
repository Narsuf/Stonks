package org.n27.stonks.presentation.common.mapping

import androidx.compose.ui.graphics.Color
import org.n27.stonks.domain.model.Rating
import org.n27.stonks.presentation.common.AppColors

internal fun Rating.toColor(): Color = when (this) {
    Rating.POSITIVE -> AppColors.Green
    Rating.CAUTION -> AppColors.Yellow
    Rating.WARNING -> AppColors.Orange
    Rating.DANGER -> AppColors.Red
}
