package org.n27.stonks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.n27.stonks.presentation.search.SearchScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        SearchScreen()
    }
}