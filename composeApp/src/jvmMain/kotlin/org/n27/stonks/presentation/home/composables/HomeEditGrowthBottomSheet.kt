package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.NumberInput
import org.n27.stonks.presentation.common.composables.PrimaryButton
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.EpsGrowthValueChanged
import org.n27.stonks.presentation.home.entities.HomeInteraction.ValuationFloorValueChanged
import org.n27.stonks.presentation.home.entities.HomeInteraction.ValuesUpdated
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
            var epsGrowth by remember(state.epsGrowthInput) { mutableStateOf(state.epsGrowthInput) }
            var valuationFloor by remember(state.valuationFloorInput) { mutableStateOf(state.valuationFloorInput) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Expected EPS Growth:",
                    style = MaterialTheme.typography.bodyLarge
                )

                NumberInput(
                    value = epsGrowth,
                    onValueChange = { onAction(EpsGrowthValueChanged(it)) },
                    maxLength = 10,
                    modifier = Modifier.fillMaxWidth(0.3f),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Valuation floor (P/E):",
                    style = MaterialTheme.typography.bodyLarge
                )

                NumberInput(
                    value = valuationFloor,
                    onValueChange = { onAction(ValuationFloorValueChanged(it)) },
                    maxLength = 10,
                    modifier = Modifier.fillMaxWidth(0.3f),
                )
            }

            PrimaryButton(
                title = "Save",
                modifier = Modifier.fillMaxWidth(),
            ) { onAction(ValuesUpdated(itemIndex, epsGrowth, valuationFloor)) }
        }
    }
}
