package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import coil3.compose.SubcomposeAsyncImage
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.AttachmentChipLinkAccent
import com.bksd.core.design_system.theme.attachmentChipLinkTile
import com.bksd.core.design_system.theme.attachmentChipPlayIcon
import com.bksd.core.design_system.theme.attachmentChipVideoTile
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.attachment.VoicePlayerPill
import com.bksd.journal.presentation.util.MomentFormatter
import kotlin.time.Instant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private fun chipLinkHost(url: String): String {
    var s = url.trim()
    val scheme = s.indexOf("://")
    if (scheme >= 0) s = s.substring(scheme + 3)
    s = s.substringBefore('/').removePrefix("www.")
    return s.ifBlank { url }
}

@Composable
fun AttachmentChips(
    attachments: ImmutableList<Attachment>,
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
    val videoChipColors = MaterialTheme.colorScheme.extended.attachmentChipVideoTile
    val linkChipTileColors = MaterialTheme.colorScheme.extended.attachmentChipLinkTile
    val playIconTint = MaterialTheme.colorScheme.extended.attachmentChipPlayIcon

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = MaterialTheme.dimens.spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)
    ) {
        attachments.forEach { attachment ->
            when (attachment) {
                is PhotoAttachment -> SubcomposeAsyncImage(
                    model = attachment.remoteUrl.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size.fab)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .background(palette.hairline),
                    error = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = palette.sub,
                                modifier = Modifier.size(MaterialTheme.dimens.icon.md)
                            )
                        }
                    }
                )

                is VideoAttachment -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size.fab)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                        .background(Brush.linearGradient(videoChipColors))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(MaterialTheme.dimens.icon.xl)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.92f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = playIconTint,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                        )
                    }
                }

                is AudioAttachment -> VoicePlayerPill(
                    playbackState = playbackState,
                    durationFormatted = if (attachment.durationMs > 0) {
                        formatter.formatDuration(attachment.durationMs)
                    } else {
                        "0:00"
                    },
                    accentColor = accentColor,
                    onPlay = onAudioPlayClick,
                    onPause = onAudioPauseClick
                )

                is LinkAttachment -> Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
                    modifier = Modifier
                        .widthIn(max = 190.dp)
                        .height(MaterialTheme.dimens.size.fab)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                        .background(chipBg)
                        .clickable { onLinkClick(attachment.url.value) }
                        .padding(start = MaterialTheme.dimens.spacing.sm, end = MaterialTheme.dimens.spacing.lg)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(MaterialTheme.dimens.icon.tile)
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                            .background(Brush.linearGradient(linkChipTileColors))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.md)
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
        Column(modifier = Modifier.background(palette.surface).padding(vertical = MaterialTheme.dimens.spacing.md)) {
            AttachmentChips(
                attachments = persistentListOf(
                    VideoAttachment(AttachmentId("1"), Url(""), 18_000L),
                    AudioAttachment(AttachmentId("2"), Url(""), 3_000L),
                    LinkAttachment(AttachmentId("3"), Url("https://www.alltrails.com/trail"))
                ),
                accentColor = AttachmentChipLinkAccent,
                formatter = object : MomentFormatter {
                    override fun formatTime(instant: Instant) = "9:41 AM"
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
