package org.n27.stonks.presentation.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.n27.stonks.presentation.common.Spacing

@Composable
fun TopBar(
    title: String? = null,
    onBack: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(Spacing.smaller),
        contentAlignment = Alignment.Center,
    ) {
        onBack?.let {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.DarkGray,
                )
            }
        }

        title?.let {
            Text(
                text = it,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
