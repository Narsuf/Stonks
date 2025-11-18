package org.n27.stonks.presentation.common.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.bigger),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = Spacing.default)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We encountered an unexpected error. Please try again later.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = Spacing.default)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRetry,
                contentPadding = PaddingValues(vertical = Spacing.small)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = Spacing.bigger),
                    text = "Retry"
                )
            }
        }
    }
}
