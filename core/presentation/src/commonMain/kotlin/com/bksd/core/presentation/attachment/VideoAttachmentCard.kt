package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.attachmentVideo
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
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
            shape = RoundedCornerShape(topStart = MaterialTheme.dimens.radius.md, topEnd = MaterialTheme.dimens.radius.md),
            onClick = onClick
        )
        return
    }
    AttachmentCardChrome(
        badgeColor = MaterialTheme.colorScheme.extended.attachmentVideo,
        badgeIcon = Icons.Default.Videocam,
        title = "Video",
        onRemove = onRemove,
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.md, end = MaterialTheme.dimens.spacing.md, bottom = MaterialTheme.dimens.spacing.md)) {
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
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            VideoAttachmentCard(durationFormatted = "0:18", onRemove = {})
        }
    }
}
