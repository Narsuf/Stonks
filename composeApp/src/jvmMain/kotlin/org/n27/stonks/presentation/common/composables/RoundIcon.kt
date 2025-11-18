package org.n27.stonks.presentation.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import java.net.URL
import javax.imageio.ImageIO

@Composable
fun RoundIcon(url: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        val bitmap: ImageBitmap? by produceState(initialValue = null, url) {
            if (url.isNotBlank()) {
                try {
                    val img = ImageIO.read(URL(url))
                    value = img?.toComposeImageBitmap()
                } catch (_: Exception) {
                    value = null
                }
            }
        }

        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
