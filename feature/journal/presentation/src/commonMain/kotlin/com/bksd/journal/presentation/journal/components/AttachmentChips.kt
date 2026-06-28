package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.journal.presentation.util.MomentFormatter

private val VideoChipColors = listOf(Color(0xFF3A3F63), Color(0xFF7682D6), Color(0xFFCF8676))
private val LinkChipTileColors = listOf(Color(0xFF8FE0CF), Color(0xFF34D399))
private val WaveformHeights = listOf(7, 12, 17, 10, 14, 8, 11, 16, 9, 7)

private fun chipLinkHost(url: String): String {
    var s = url.trim()
    val scheme = s.indexOf("://")
    if (scheme >= 0) s = s.substring(scheme + 3)
    s = s.substringBefore('/').removePrefix("www.")
    return s.ifBlank { url }
}

@Composable
fun AttachmentChips(
    attachments: List<Attachment>,
    accentColor: Color,
    formatter: MomentFormatter,
    playbackState: PlaybackState,
    onAudioPlayClick: () -> Unit,
    onAudioPauseClick: () -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (attachments.isEmpty()) return
    val palette = rememberNewEntryPalette()
    val chipBg = lerp(palette.surface, palette.text, 0.06f)
    val idleBar = palette.sub.copy(alpha = 0.4f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        attachments.forEach { attachment ->
            when (attachment) {
                is PhotoAttachment -> AsyncImage(
                    model = attachment.remoteUrl.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(palette.hairline)
                )

                is VideoAttachment -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(Brush.linearGradient(VideoChipColors))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.92f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color(0xFF1C1B1A),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                is AudioAttachment -> {
                    val isPlaying = playbackState == PlaybackState.PLAYING
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                        modifier = Modifier
                            .height(54.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(chipBg)
                            .padding(start = 8.dp, end = 14.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(accentColor)
                                .clickable { if (isPlaying) onAudioPauseClick() else onAudioPlayClick() }
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            WaveformHeights.forEachIndexed { index, h ->
                                Box(
                                    modifier = Modifier
                                        .width(2.5.dp)
                                        .height(h.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(if (index < 4) accentColor else idleBar)
                                )
                            }
                        }
                        Text(
                            text = if (attachment.durationMs > 0) formatter.formatDuration(
                                attachment.durationMs
                            ) else "0:00",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = palette.sub
                        )
                    }
                }

                is LinkAttachment -> Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .widthIn(max = 190.dp)
                        .height(54.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(chipBg)
                        .clickable { onLinkClick(attachment.url.value) }
                        .padding(start = 8.dp, end = 15.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(11.dp))
                            .background(Brush.linearGradient(LinkChipTileColors))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    Text(
                        text = chipLinkHost(attachment.url.value),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AttachmentChipsPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Column(modifier = Modifier.background(palette.surface).padding(vertical = 12.dp)) {
            AttachmentChips(
                attachments = listOf(
                    VideoAttachment(AttachmentId("1"), Url(""), 18_000L),
                    AudioAttachment(AttachmentId("2"), Url(""), 3_000L),
                    LinkAttachment(AttachmentId("3"), Url("https://www.alltrails.com/trail"))
                ),
                accentColor = Color(0xFFA78BFA),
                formatter = object : MomentFormatter {
                    override fun formatTime(instant: kotlin.time.Instant) = "9:41 AM"
                    override fun formatDuration(ms: Long) = "0:03"
                },
                playbackState = PlaybackState.STOPPED,
                onAudioPlayClick = {},
                onAudioPauseClick = {},
                onLinkClick = {}
            )
        }
    }
}
