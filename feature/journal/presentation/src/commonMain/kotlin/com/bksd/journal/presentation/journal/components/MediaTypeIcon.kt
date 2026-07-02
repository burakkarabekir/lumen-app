package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.mediaAudio
import com.bksd.core.design_system.theme.mediaLink
import com.bksd.core.design_system.theme.mediaPhoto
import com.bksd.core.design_system.theme.mediaText
import com.bksd.core.design_system.theme.mediaVideo


@Composable
internal fun MediaTypeIcon(type: CardMediaType) {
    val (icon, bgColor) = when (type) {
        CardMediaType.PHOTO -> Icons.Default.CameraAlt to MaterialTheme.colorScheme.extended.mediaPhoto
        CardMediaType.VIDEO -> Icons.Default.Videocam to MaterialTheme.colorScheme.extended.mediaVideo
        CardMediaType.AUDIO -> Icons.Default.Mic to MaterialTheme.colorScheme.extended.mediaAudio
        CardMediaType.LINK -> Icons.Default.Link to MaterialTheme.colorScheme.extended.mediaLink
        CardMediaType.TEXT -> Icons.AutoMirrored.Filled.TextSnippet to MaterialTheme.colorScheme.extended.mediaText
    }

    Box(
        modifier = Modifier
            .size(MaterialTheme.dimens.icon.avatar)
            .clip(CircleShape)
            .background(bgColor.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type.name,
            tint = bgColor,
            modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
        )
    }
}

