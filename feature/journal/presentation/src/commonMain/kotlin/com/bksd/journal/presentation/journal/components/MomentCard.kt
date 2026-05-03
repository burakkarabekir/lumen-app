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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.presentation.util.MomentFormatter


@Composable
fun MomentCard(
    moment: Moment,
    formatter: MomentFormatter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    audioPlaybackState: PlaybackState = PlaybackState.STOPPED,
    audioCurrentPosition: String = "0:00",
    onAudioPlayClick: () -> Unit = {},
    onAudioPauseClick: () -> Unit = {},
) {
    val formattedTime = remember(moment.createdAt) {
        formatter.formatTime(moment.createdAt)
    }

    // Determine primary media type for the header icon
    val primaryType = remember(moment.attachments) {
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
                when (val primary = attachments.first()) {
                    is AudioAttachment -> {
                        if (!body.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AudioPreview(
                            playbackState = audioPlaybackState,
                            currentPositionFormatted = audioCurrentPosition,
                            mode = AudioPlaybackMode.COMPACT,
                            durationFormatted = if (primary.durationMs > 0)
                                formatter.formatDuration(primary.durationMs) else "0:00",
                            onPlayClick = onAudioPlayClick,
                            onPauseClick = onAudioPauseClick
                        )
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

                    is PhotoAttachment -> {
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
                        PhotoPreview(url = primary.remoteUrl.value)
                    }

                    is VideoAttachment -> {
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
                        VideoPreview(durationMs = primary.durationMs, formatter = formatter)
                    }

                    is LinkAttachment -> {
                        if (body != null) {
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
                        LinkPreview(url = primary.url.value)
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
