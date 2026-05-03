package com.bksd.journal.presentation.journal.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.AudioPlaybackMode
import com.bksd.core.presentation.AudioPlaybackStrip
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun AudioPreview(
    playbackState: PlaybackState,
    currentPositionFormatted: String,
    durationFormatted: String,
    mode: AudioPlaybackMode = AudioPlaybackMode.STANDARD,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
) {
    AudioPlaybackStrip(
        playbackState = playbackState,
        amplitudes = persistentListOf(),
        durationFormatted = durationFormatted,
        onPlayClick = onPlayClick,
        onPauseClick = onPauseClick,
        mode = mode,
        currentPositionFormatted = currentPositionFormatted
    )
}

@Preview
@Composable
private fun AudioPreviewPreview() {
    AppTheme {
        AudioPreview(
            playbackState = PlaybackState.PLAYING,
            currentPositionFormatted = "0:42",
            durationFormatted = "2:14",
            onPlayClick = {},
            onPauseClick = {}
        )
    }
}

@Preview
@Composable
private fun AudioPreviewPreviewDark() {
    AppTheme(darkTheme = true) {
        AudioPreview(
            playbackState = PlaybackState.PAUSED,
            currentPositionFormatted = "0:00",
            durationFormatted = "1:45",
            onPlayClick = {},
            onPauseClick = {}
        )
    }
}