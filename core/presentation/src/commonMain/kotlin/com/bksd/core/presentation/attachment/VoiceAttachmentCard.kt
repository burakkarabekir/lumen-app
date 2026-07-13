package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.attachmentVoice
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.AudioPlaybackStrip
import com.bksd.core.presentation.Res
import com.bksd.core.presentation.attachment_label_voice
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@Composable
fun VoiceAttachmentCard(
    playbackState: PlaybackState,
    amplitudes: ImmutableList<Float>,
    positionFormatted: String,
    durationFormatted: String,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    compact: Boolean = false
) {
    if (compact) {
        AudioPlaybackStrip(
            playbackState = playbackState,
            amplitudes = amplitudes,
            durationFormatted = durationFormatted,
            onPlayClick = onPlay,
            onPauseClick = onPause,
            mode = AudioPlaybackMode.STANDARD,
            currentPositionFormatted = positionFormatted,
            modifier = modifier
        )
        return
    }
    val palette = rememberNewEntryPalette()
    AttachmentCardChrome(
        badgeColor = MaterialTheme.colorScheme.extended.attachmentVoice,
        badgeIcon = Icons.Default.Mic,
        title = stringResource(Res.string.attachment_label_voice),
        onRemove = onRemove,
        modifier = modifier
    ) {
        VoicePlayerPill(
            playbackState = playbackState,
            durationFormatted = durationFormatted,
            accentColor = palette.saveBg,
            onPlay = onPlay,
            onPause = onPause,
            modifier = Modifier.padding(
                start = MaterialTheme.dimens.spacing.lg,
                end = MaterialTheme.dimens.spacing.lg,
                bottom = MaterialTheme.dimens.spacing.lg
            )
        )
    }
}

@Preview
@Composable
private fun VoiceAttachmentCardPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            VoiceAttachmentCard(
                playbackState = PlaybackState.STOPPED,
                amplitudes = persistentListOf(0.2f, 0.6f, 0.9f, 0.4f, 0.7f, 0.3f, 0.8f),
                positionFormatted = "0:15",
                durationFormatted = "0:42",
                onPlay = {},
                onPause = {},
                onRemove = {}
            )
        }
    }
}
