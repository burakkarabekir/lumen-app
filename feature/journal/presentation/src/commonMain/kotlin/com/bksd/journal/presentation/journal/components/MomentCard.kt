package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.extended
import com.bksd.core.domain.model.MediaType
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun MomentCard(
    moment: Moment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedTime = remember(moment.createdAt) {
        formatTimestamp(moment.createdAt)
    }

    // Determine primary media type for the header icon
    val primaryType = remember(moment.attachments, moment.links) {
        determinePrimaryType(moment)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            // Header: [Media Type Icon] [Time] ... [Mood Tag]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Media type icon circle
                    MediaTypeIcon(type = primaryType)

                    Text(
                        text = formattedTime,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                MoodTag(mood = moment.mood)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title / Body excerpt
            val body = moment.body
            if (!body.isNullOrBlank()) {
                // Title-like first line
                val titleText = body.take(40).let { if (body.length > 40) "$it…" else it }
                Text(
                    text = titleText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = "Untitled Moment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Media preview — between title and body text per design
            val attachments = moment.attachments
            if (attachments.isNotEmpty()) {
                val primary = attachments.first()
                when (primary.type) {
                    MediaType.AUDIO -> {
                        if (!body.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AudioPreview(durationMs = primary.durationMs)
                        // Body text shown below audio as quote
                        if (body != null && body.length > 40) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "\"${body.drop(40).take(80).trim()}…\"",
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    MediaType.PHOTO, MediaType.VIDEO -> {
                        if (body != null && body.length > 40) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = body.drop(40).take(100).trim().let {
                                    if (body.length > 140) "$it…" else it
                                },
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (primary.type == MediaType.PHOTO) {
                            PhotoPreview()
                        } else {
                            VideoPreview(durationMs = primary.durationMs)
                        }
                    }

                    MediaType.LINK -> {

                    }
                }
            } else {
                // Text-only: show body excerpt
                if (body != null && body.length > 40) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = body.drop(40).take(100).trim().let {
                            if (body.length > 140) "$it…" else it
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Tags
            if (moment.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    moment.tags.forEach { tag ->
                        Text(
                            text = "#$tag",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// -- Media type icon circle in header --

@Composable
private fun MediaTypeIcon(type: CardMediaType) {
    val (icon, bgColor) = when (type) {
        CardMediaType.PHOTO -> Icons.Default.CameraAlt to Color(0xFF2563EB)
        CardMediaType.VIDEO -> Icons.Default.Videocam to Color(0xFF7C3AED)
        CardMediaType.AUDIO -> Icons.Default.Mic to Color(0xFF9333EA)
        CardMediaType.TEXT -> Icons.Default.TextSnippet to Color(0xFF475569)
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

private enum class CardMediaType { PHOTO, VIDEO, AUDIO, TEXT }

private fun determinePrimaryType(moment: Moment): CardMediaType {
    val first = moment.attachments.firstOrNull()
    return when {
        first?.type == MediaType.PHOTO -> CardMediaType.PHOTO
        first?.type == MediaType.VIDEO -> CardMediaType.VIDEO
        first?.type == MediaType.AUDIO -> CardMediaType.AUDIO
        else -> CardMediaType.TEXT
    }
}

// -- Media previews --

@Composable
private fun PhotoPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Photo",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun VideoPreview(durationMs: Long?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Video",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(28.dp)
            )
        }

        if (durationMs != null && durationMs > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = formatDuration(durationMs),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun AudioPreview(durationMs: Long?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Play icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Audio",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        // Waveform placeholder bars
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val barHeights =
                listOf(8, 14, 20, 12, 24, 16, 10, 22, 14, 8, 18, 12, 20, 10, 16, 24, 12, 8, 14, 10)
            barHeights.forEach { h ->
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(h.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                )
            }
        }

        // Duration
        Text(
            text = if (durationMs != null && durationMs > 0) formatDuration(durationMs) else "0:00",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// -- Mood tag --

@Composable
fun MoodTag(mood: Mood) {
    val extendedColors = MaterialTheme.colorScheme.extended
    val (backgroundColor, textColor) = when (mood) {
        Mood.ENERGETIC -> extendedColors.emotionJoyBg to extendedColors.emotionJoy
        Mood.REFLECTIVE -> extendedColors.emotionSurpriseBg to extendedColors.emotionSurprise
        Mood.CALM -> extendedColors.emotionCalmBg to extendedColors.emotionCalm
        Mood.TIRED -> extendedColors.emotionSadnessBg to extendedColors.emotionSadness
        Mood.INSPIRED -> extendedColors.emotionGratitudeBg to extendedColors.emotionGratitude
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = mood.emoji, fontSize = 12.sp)
        Text(
            text = mood.label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

// -- Utilities --

private fun formatTimestamp(instant: Instant): String {
    return try {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = if (local.hour % 12 == 0) 12 else local.hour % 12
        val amPm = if (local.hour < 12) "AM" else "PM"
        val minute = local.minute.toString().padStart(2, '0')
        "$hour:$minute $amPm"
    } catch (_: Exception) {
        ""
    }
}

private fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}
