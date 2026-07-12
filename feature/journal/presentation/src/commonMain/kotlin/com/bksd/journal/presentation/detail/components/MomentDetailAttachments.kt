package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AttachmentId
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.Url
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.attachment.LinkAttachmentCard
import com.bksd.core.presentation.attachment.PhotoAttachmentCard
import com.bksd.core.presentation.attachment.VideoAttachmentCard
import com.bksd.core.presentation.attachment.VoiceAttachmentCard
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.attachments_section
import com.bksd.journal.presentation.util.MomentFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun MomentDetailAttachments(
    attachments: List<Attachment>,
    audioPlaybackState: PlaybackState,
    audioCurrentPosition: String,
    audioDuration: String,
    audioAmplitudes: ImmutableList<Float>,
    onAudioPlayClick: () -> Unit,
    onAudioPauseClick: () -> Unit,
    formatter: MomentFormatter,
    modifier: Modifier = Modifier,
    onRemove: ((Attachment) -> Unit)? = null,
    onLinkClick: ((String) -> Unit)? = null
) {
    if (attachments.isEmpty()) return
    val palette = rememberNewEntryPalette()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.spacing.xxs),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.attachments_section),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.7.sp,
                color = palette.sub
            )
            Text(
                text = attachments.size.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = palette.sub
            )
        }

        attachments.forEach { attachment ->
            val remove: (() -> Unit)? = onRemove?.let { { it(attachment) } }
            when (attachment) {
                is PhotoAttachment -> PhotoAttachmentCard(
                    imageModel = attachment.remoteUrl.value,
                    onRemove = remove
                )

                is VideoAttachment -> VideoAttachmentCard(
                    durationFormatted = if (attachment.durationMs > 0) {
                        formatter.formatDuration(attachment.durationMs)
                    } else "",
                    onRemove = remove
                )

                is AudioAttachment -> VoiceAttachmentCard(
                    playbackState = audioPlaybackState,
                    amplitudes = audioAmplitudes,
                    positionFormatted = audioCurrentPosition,
                    durationFormatted = if (attachment.durationMs > 0) {
                        formatter.formatDuration(attachment.durationMs)
                    } else audioDuration,
                    onPlay = onAudioPlayClick,
                    onPause = onAudioPauseClick,
                    onRemove = remove
                )

                is LinkAttachment -> LinkAttachmentCard(
                    url = attachment.url.value,
                    onRemove = remove,
                    onClick = onLinkClick?.let { cb -> { cb(attachment.url.value) } }
                )
            }
        }
    }
}

@Preview
@Composable
private fun MomentDetailAttachmentsPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            MomentDetailAttachments(
                attachments = listOf(
                    PhotoAttachment(AttachmentId("1"), Url("")),
                    LinkAttachment(AttachmentId("2"), Url("https://www.alltrails.com/trail"))
                ),
                audioPlaybackState = PlaybackState.STOPPED,
                audioCurrentPosition = "0:00",
                audioDuration = "0:42",
                audioAmplitudes = persistentListOf(),
                onAudioPlayClick = {},
                onAudioPauseClick = {},
                formatter = object : MomentFormatter {
                    override fun formatTime(instant: Instant) = "9:41 AM"
                    override fun formatDuration(ms: Long) = "0:18"
                },
                onRemove = {}
            )
        }
    }
}
