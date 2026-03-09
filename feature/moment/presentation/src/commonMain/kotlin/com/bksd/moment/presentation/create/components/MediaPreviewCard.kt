package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.model.MediaType

@Composable
fun MediaPreviewCard(
    type: MediaType,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (type == MediaType.VIDEO) Icons.Default.Videocam else Icons.Default.Image
    val title = if (type == MediaType.VIDEO) "Video Attached" else "Photo Attached"

    AttachmentCardLayout(
        headerIcon = icon,
        headerTitle = title,
        onDeleteClick = onRemoveClick,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
            contentAlignment = Alignment.Center
        ) {
            if (type == MediaType.VIDEO) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Video",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPhoto() {
    AppTheme {
        MediaPreviewCard(
            type = MediaType.PHOTO,
            onRemoveClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewVideo() {
    AppTheme(darkTheme = true) {
        MediaPreviewCard(
            type = MediaType.VIDEO,
            onRemoveClick = {}
        )
    }
}