package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette

@Composable
fun VideoAttachmentCard(
    durationFormatted: String,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    compact: Boolean = false
) {
    if (compact) {
        VideoThumbnail(
            durationFormatted = durationFormatted,
            height = 160.dp,
            modifier = modifier,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            onClick = onClick
        )
        return
    }
    AttachmentCardChrome(
        badgeColor = VideoBadgeColor,
        badgeIcon = Icons.Default.Videocam,
        title = "Video",
        onRemove = onRemove,
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)) {
            VideoThumbnail(
                durationFormatted = durationFormatted,
                height = 158.dp,
                onClick = onClick
            )
        }
    }
}

@Preview
@Composable
private fun VideoAttachmentCardPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(16.dp)) {
            VideoAttachmentCard(durationFormatted = "0:18", onRemove = {})
        }
    }
}
