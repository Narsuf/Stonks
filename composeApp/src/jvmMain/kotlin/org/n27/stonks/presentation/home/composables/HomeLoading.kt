package org.n27.stonks.presentation.home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.ShimmerBone
import org.n27.stonks.presentation.search.composables.SearchCellShimmer

@Composable
internal fun HomeLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.default),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShimmerBone(
            width = 120.dp,
            height = 48.dp,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(Modifier.height(Spacing.default))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.default)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ShimmerBone(width = 120.dp, height = Spacing.big)
                Spacer(Modifier.width(Spacing.smaller))
                ShimmerBone(width = Spacing.biggest, height = Spacing.big)
            }

            ShimmerBone(width = 140.dp, height = 20.dp)
        }

        Spacer(Modifier.height(Spacing.default))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.smaller)
        ) {
            items(5) { SearchCellShimmer() }
        }
    }
}
