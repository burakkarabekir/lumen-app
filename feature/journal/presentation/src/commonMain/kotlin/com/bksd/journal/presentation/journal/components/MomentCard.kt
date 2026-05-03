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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.labelXSmall
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.util.onSafe
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood
import com.bksd.journal.presentation.util.DefaultMomentFormatter
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun MomentCard(
    moment: Moment,
    formatter: MomentFormatter,
    timeZone: TimeZone,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    audioPlaybackState: PlaybackState = PlaybackState.STOPPED,
    audioCurrentPosition: String = "0:00",
    onAudioPlayClick: () -> Unit = {},
    onAudioPauseClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
) {
    val formattedDate = remember(moment.createdAt) {
        formatCardDate(moment.createdAt, timeZone)
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(2.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        MediaContent(
            moment = moment,
            formatter = formatter,
            audioPlaybackState = audioPlaybackState,
            audioCurrentPosition = audioCurrentPosition,
            onAudioPlayClick = onAudioPlayClick,
            onAudioPauseClick = onAudioPauseClick
        )

        // ── 2. Location Bar (between media and text) ──
        moment.location?.let { location ->
            LocationBar(location = location)
        }

        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(
                text = moment.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            moment.body.onSafe {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            moment.tags.onSafe { items ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items.forEach { tag ->
                        Text(
                            text = "#$tag",
                            style = MaterialTheme.typography.labelXSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // ── 4. Footer: Date + Actions Menu ──
        AppDivider()
        CardFooter(
            formattedDate = formattedDate,
            onMoreClick = onMoreClick
        )
    }
}


@Preview
@Composable
fun Preview() {
    AppTheme {
        MomentCard(
            moment = Moment(
                id = "0",
                title = "title test",
                body = "body test",
                createdAt = Clock.System.now(),
                mood = Mood.REFLECTIVE
            ),
            formatter = DefaultMomentFormatter(TimeZone.UTC),
            onClick = {},
            timeZone = TimeZone.UTC
        )
    }
}

@Preview
@Composable
fun PreviewDark() {
    AppTheme(darkTheme = true) {
        MomentCard(
            moment = Moment(
                id = "0",
                title = "title test",
                body = "body test",
                tags = listOf("tag1", "tag2"),
                createdAt = Clock.System.now(),
                mood = Mood.REFLECTIVE
            ),
            formatter = DefaultMomentFormatter(TimeZone.UTC),
            onClick = {},
            timeZone = TimeZone.UTC
        )
    }
}

// ─────────────────────── Media Content ───────────────────────

@Composable
private fun MediaContent(
    moment: Moment,
    formatter: MomentFormatter,
    audioPlaybackState: PlaybackState,
    audioCurrentPosition: String,
    onAudioPlayClick: () -> Unit,
    onAudioPauseClick: () -> Unit
) {
    val attachments = moment.attachments
    if (attachments.isEmpty()) return

    when (val primary = attachments.first()) {
        is PhotoAttachment -> {
            PhotoPreview(url = primary.remoteUrl.value)
        }

        is VideoAttachment -> {
            VideoPreview(durationMs = primary.durationMs, formatter = formatter)
        }

        is AudioAttachment -> {
            Box(modifier = Modifier.padding(12.dp)) {
                AudioPreview(
                    playbackState = audioPlaybackState,
                    currentPositionFormatted = audioCurrentPosition,
                    mode = AudioPlaybackMode.STANDARD,
                    durationFormatted = if (primary.durationMs > 0)
                        formatter.formatDuration(primary.durationMs) else "0:00",
                    onPlayClick = onAudioPlayClick,
                    onPauseClick = onAudioPauseClick
                )
            }
        }

        is LinkAttachment -> {
            Box(modifier = Modifier.padding(12.dp)) {
                LinkPreview(url = primary.url.value)
            }
        }
    }
}

// ─────────────────────── Location Bar ───────────────────────

@Composable
private fun LocationBar(location: LocationData) {
    val displayName = location.displayName ?: return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 4.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = displayName,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.tertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


// ─────────────────────── Footer ───────────────────────

@Composable
private fun CardFooter(
    formattedDate: String,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        IconButton(
            onClick = onMoreClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More actions",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─────────────────────── Helpers ───────────────────────

/**
 * Formats an Instant to "DayOfWeek, Month Day" (e.g., "Sunday, May 3").
 */
private fun formatCardDate(instant: kotlin.time.Instant, timeZone: TimeZone): String {
    val ldt = instant.toLocalDateTime(timeZone)
    val dayOfWeek = ldt.dayOfWeek.name
        .lowercase()
        .replaceFirstChar { it.uppercase() }
    val month = ldt.month.name
        .lowercase()
        .replaceFirstChar { it.uppercase() }
    return "$dayOfWeek, $month ${ldt.date.day}"
}
