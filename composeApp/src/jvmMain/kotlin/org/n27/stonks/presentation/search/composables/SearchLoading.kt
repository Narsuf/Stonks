package org.n27.stonks.presentation.search.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.shimmer.ShimmerBone
import org.n27.stonks.presentation.common.composables.shimmer.ShimmerCellList

@Composable
internal fun SearchLoading() {
    Column(modifier = Modifier.fillMaxSize()) {
        ShimmerBone(
            height = 56.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.default)
        )

        ShimmerCellList()
    }
}
