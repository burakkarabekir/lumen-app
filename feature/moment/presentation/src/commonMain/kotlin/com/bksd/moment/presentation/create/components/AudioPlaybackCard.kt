package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.AudioPlaybackStrip
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Post-recording playback card with play/pause button, waveform visualizer
 * showing playback progress, duration, and a delete action.
 *
 * Wraps [AudioPlaybackStrip] in STANDARD mode with an [AttachmentCardLayout]
 * header providing the delete action.
 */
@Composable
fun AudioPlaybackCard(
    playbackState: PlaybackState,
    amplitudes: ImmutableList<Float>,
    currentPositionFormatted: String,
    durationFormatted: String,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AttachmentCardLayout(
        headerIcon = Icons.Default.Mic,
        headerTitle = "Voice Record",
        onDeleteClick = onDeleteClick,
        modifier = modifier
    ) {
        AudioPlaybackStrip(
            playbackState = playbackState,
            amplitudes = amplitudes,
            durationFormatted = durationFormatted,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            mode = AudioPlaybackMode.STANDARD,
            currentPositionFormatted = currentPositionFormatted,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

// ==================== Previews ====================

@Preview
@Composable
private fun PreviewStopped() {
    AppTheme {
        AudioPlaybackCard(
            playbackState = PlaybackState.STOPPED,
            amplitudes = persistentListOf(
                0.2f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f
            ),
            currentPositionFormatted = "0:00",
            durationFormatted = "1:23",
            onPlayClick = {},
            onPauseClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewPlaying() {
    AppTheme {
        AudioPlaybackCard(
            playbackState = PlaybackState.PLAYING,
            amplitudes = persistentListOf(
                0.2f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f,
                0.2f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f
            ),
            currentPositionFormatted = "0:45",
            durationFormatted = "1:23",
            onPlayClick = {},
            onPauseClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AudioPlaybackCard(
            playbackState = PlaybackState.PAUSED,
            amplitudes = persistentListOf(
                0.2f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f
            ),
            currentPositionFormatted = "0:30",
            durationFormatted = "1:23",
            onPlayClick = {},
            onPauseClick = {},
            onDeleteClick = {}
        )
    }
}
