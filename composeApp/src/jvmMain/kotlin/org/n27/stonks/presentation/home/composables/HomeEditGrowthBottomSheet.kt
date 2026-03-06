package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.NumberInput
import org.n27.stonks.presentation.common.composables.PrimaryButton
import org.n27.stonks.presentation.home.entities.HomeInteraction
import org.n27.stonks.presentation.home.entities.HomeInteraction.ValuationFloorValueChanged
import org.n27.stonks.presentation.home.entities.HomeInteraction.ValuesUpdated
import org.n27.stonks.presentation.home.entities.HomeState
import stonks.composeapp.generated.resources.Res
import stonks.composeapp.generated.resources.save
import stonks.composeapp.generated.resources.valuation_floor

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
            var valuationFloor by remember(state.bottomSheet.valuationFloorInput) {
                mutableStateOf(state.bottomSheet.valuationFloorInput)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.valuation_floor),
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
                title = stringResource(Res.string.save),
                modifier = Modifier.fillMaxWidth(),
            ) { onAction(ValuesUpdated(itemIndex, valuationFloor)) }
        }
    }
}
