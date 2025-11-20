package org.n27.stonks.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.TopBar

@Composable
internal fun DetailScreen() {
    Column {
        TopBar { }

        Spacer(Modifier.height(Spacing.default))

        Text("Detail")
    }
}
