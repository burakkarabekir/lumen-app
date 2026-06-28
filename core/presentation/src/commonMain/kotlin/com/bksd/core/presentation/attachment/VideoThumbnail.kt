package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme

private val VideoCoverColors = listOf(Color(0xFF3A3F63), Color(0xFF7682D6), Color(0xFFCF8676))

@Composable
internal fun VideoThumbnail(
    durationFormatted: String,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    0f to VideoCoverColors[0],
                    0.6f to VideoCoverColors[1],
                    1f to VideoCoverColors[2]
                )
            )
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.55f to Color.Transparent,
                        1f to Color(0x52000000)
                    )
                )
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.92f))
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color(0xFF1C1B1A),
                modifier = Modifier.size(24.dp)
            )
        }
        if (durationFormatted.isNotBlank()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(11.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color(0x8C000000))
                    .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
                Text(
                    text = durationFormatted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun VideoThumbnailPreview() {
    AppTheme(darkTheme = true) {
        VideoThumbnail(
            durationFormatted = "0:18",
            height = 158.dp,
            modifier = Modifier.padding(16.dp)
        )
    }
}
