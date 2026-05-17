package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.model.Attachment
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.AudioPlaybackStrip
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.attachments_section
import com.bksd.journal.presentation.journal.components.LinkPreview
import com.bksd.journal.presentation.journal.components.VideoPreview
import com.bksd.journal.presentation.util.MomentFormatter
import com.bksd.journal.presentation.voice_reflection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

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
    modifier: Modifier = Modifier
) {
    if (attachments.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.attachments_section),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.5.sp
        )

        attachments.forEach { attachment ->
            when (attachment) {
                is PhotoAttachment -> DetailPhotoAttachment(url = attachment.remoteUrl.value)
                is VideoAttachment -> VideoPreview(
                    durationMs = attachment.durationMs,
                    formatter = formatter
                )

                is AudioAttachment -> DetailVoicePlaybackCard(
                    playbackState = audioPlaybackState,
                    currentPositionFormatted = audioCurrentPosition,
                    durationFormatted = audioDuration,
                    amplitudes = audioAmplitudes,
                    onPlayClick = onAudioPlayClick,
                    onPauseClick = onAudioPauseClick
                )

                is LinkAttachment -> LinkPreview(url = attachment.url.value)
            }
        }
    }
}

@Composable
private fun DetailPhotoAttachment(
    url: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(4f / 3f),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun DetailVoicePlaybackCard(
    playbackState: PlaybackState,
    currentPositionFormatted: String,
    durationFormatted: String,
    amplitudes: ImmutableList<Float>,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = stringResource(Res.string.voice_reflection),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        AudioPlaybackStrip(
            playbackState = playbackState,
            amplitudes = amplitudes,
            durationFormatted = durationFormatted,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            mode = AudioPlaybackMode.STANDARD,
            currentPositionFormatted = currentPositionFormatted
        )
    }
}

@Preview
@Composable
private fun DetailVoicePlaybackCardPreview() {
    AppTheme {
        DetailVoicePlaybackCard(
            playbackState = PlaybackState.STOPPED,
            currentPositionFormatted = "0:00",
            durationFormatted = "0:42",
            amplitudes = persistentListOf(0.2f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f),
            onPlayClick = {},
            onPauseClick = {}
        )
    }
}
