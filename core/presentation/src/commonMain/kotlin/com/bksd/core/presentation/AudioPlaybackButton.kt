package com.bksd.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.paused
import com.bksd.core.design_system.playing
import com.bksd.core.design_system.stopped
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.util.defaultShadow
import com.bksd.core.presentation.model.PlaybackState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import com.bksd.core.design_system.Res as DesignSystemR

@Composable
fun AudioPlaybackButton(
    playbackState: PlaybackState,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
) {
    FilledIconButton(
        onClick = when (playbackState) {
            PlaybackState.PLAYING -> onPauseClick
            PlaybackState.PAUSED,
            PlaybackState.STOPPED -> onPlayClick
        },
        modifier = modifier.defaultShadow(),
        colors = colors,
    ) {
        Icon(
            imageVector = when (playbackState) {
                PlaybackState.PLAYING -> vectorResource(Res.drawable.ic_pause)
                PlaybackState.PAUSED,
                PlaybackState.STOPPED -> Icons.Filled.PlayArrow
            },
            contentDescription = when (playbackState) {
                PlaybackState.PLAYING -> stringResource(DesignSystemR.string.playing)
                PlaybackState.PAUSED -> stringResource(DesignSystemR.string.paused)
                PlaybackState.STOPPED -> stringResource(DesignSystemR.string.stopped)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        AudioPlaybackButton(
            playbackState = PlaybackState.PLAYING,
            onPlayClick = {},
            onPauseClick = {},
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.Magenta
            ),
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AudioPlaybackButton(
            playbackState = PlaybackState.PAUSED,
            onPlayClick = {},
            onPauseClick = {},
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.Red
            ),
        )
    }
}

@Preview
@Composable
private fun PreviewStoppedDark() {
    AppTheme(darkTheme = true) {
        AudioPlaybackButton(
            playbackState = PlaybackState.STOPPED,
            onPlayClick = {},
            onPauseClick = {},
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color.Blue
            ),
        )
    }
}