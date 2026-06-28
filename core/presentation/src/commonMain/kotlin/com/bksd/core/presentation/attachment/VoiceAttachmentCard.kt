package com.bksd.core.presentation.attachment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.AudioPlaybackStrip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
        badgeColor = VoiceBadgeColor,
        badgeIcon = Icons.Default.Mic,
        title = "Voice memo",
        onRemove = onRemove,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(palette.hairline)
        )
        AudioPlaybackStrip(
            playbackState = playbackState,
            amplitudes = amplitudes,
            durationFormatted = durationFormatted,
            onPlayClick = onPlay,
            onPauseClick = onPause,
            mode = AudioPlaybackMode.STANDARD,
            currentPositionFormatted = positionFormatted,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp)
        )
    }
}

@Preview
@Composable
private fun VoiceAttachmentCardPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Box(modifier = Modifier.background(palette.pageBg).padding(16.dp)) {
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
