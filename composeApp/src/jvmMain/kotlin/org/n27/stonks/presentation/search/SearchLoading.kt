package org.n27.stonks.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.n27.stonks.presentation.common.Spacing
import org.n27.stonks.presentation.common.composables.shimmerLoading

@Composable
fun SearchLoading() {
    Column(modifier = Modifier.fillMaxSize()) {

        ShimmerBone(
            height = 56.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.default)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.default)
        ) {
            items(7) {
                SearchCellShimmer()
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun SearchCellShimmer() {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ShimmerBone(
            width = 40.dp,
            height = 40.dp,
            shape = CircleShape
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            ShimmerBone(width = 180.dp, height = 18.dp)
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBone(width = 80.dp, height = 14.dp)
        }

        ShimmerBone(width = 70.dp, height = 18.dp)
    }
}

@Composable
fun ShimmerBone(
    width: Dp = Dp.Unspecified,
    height: Dp,
    shape: Shape = RoundedCornerShape(4.dp),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .then(
                if (width != Dp.Unspecified)
                    Modifier.width(width)
                else
                    Modifier
            )
            .height(height)
            .clip(shape)
            .shimmerLoading()
    )
}