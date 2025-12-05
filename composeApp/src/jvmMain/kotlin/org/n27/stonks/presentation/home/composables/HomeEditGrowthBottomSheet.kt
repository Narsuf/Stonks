package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.NumberInput
import org.n27.stonks.presentation.common.composables.PrimaryButton
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.ValueChanged
import org.n27.stonks.presentation.home.entities.HomeInteraction.ValueUpdated
import org.n27.stonks.presentation.home.entities.HomeState

@Composable
internal fun HomeEditGrowthBottomSheet(
    itemIndex: Int?,
    state: HomeState,
    onAction: (action: HomeInteraction) -> Unit,
) {
    if (state is HomeState.Content && itemIndex != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.default)
                .padding(bottom = Spacing.smaller),
            verticalArrangement = Arrangement.spacedBy(Spacing.default),
        ) {
            Text(
                text = "Expected EPS Growth",
                style = MaterialTheme.typography.titleMedium
            )

            var value by remember(state.input) { mutableStateOf(state.input) }

            NumberInput(
                value = value,
                onValueChange = { onAction(ValueChanged(it)) },
                maxLength = 10,
                modifier = Modifier.fillMaxWidth(0.3f),
            )

            PrimaryButton(
                title = "Save",
                modifier = Modifier.fillMaxWidth(),
            ) { onAction(ValueUpdated(itemIndex, value)) }
        }
    }
}
