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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.domain.model.Moment

internal enum class CardMediaType { PHOTO, VIDEO, AUDIO, LINK, TEXT }

@Composable
internal fun MediaTypeIcon(type: CardMediaType) {
    val (icon, bgColor) = when (type) {
        CardMediaType.PHOTO -> Icons.Default.CameraAlt to Color(0xFF2563EB)
        CardMediaType.VIDEO -> Icons.Default.Videocam to Color(0xFF7C3AED)
        CardMediaType.AUDIO -> Icons.Default.Mic to Color(0xFF9333EA)
        CardMediaType.LINK -> Icons.Default.Link to Color(0xFF0EA5E9)
        CardMediaType.TEXT -> Icons.AutoMirrored.Filled.TextSnippet to Color(0xFF475569)
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(bgColor.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type.name,
            tint = bgColor,
            modifier = Modifier.size(16.dp)
        )
    }
}

internal fun determinePrimaryType(moment: Moment): CardMediaType {
    val first = moment.attachments.firstOrNull()
    return when (first) {
        is PhotoAttachment -> CardMediaType.PHOTO
        is VideoAttachment -> CardMediaType.VIDEO
        is AudioAttachment -> CardMediaType.AUDIO
        is LinkAttachment -> CardMediaType.LINK
        else -> CardMediaType.TEXT
    }
}
