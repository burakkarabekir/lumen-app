package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.visualizer.VisualizerStyle
import com.bksd.core.design_system.component.visualizer.VoiceVisualizer
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.presentation.AudioPlaybackButton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Post-recording playback card with play/pause button, waveform visualizer
 * showing playback progress, duration, and a delete action.
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Play/Pause Button
        AudioPlaybackButton(
            playbackState = playbackState,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            modifier = Modifier.size(40.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Visualizer + Time
        Column(
            modifier = Modifier.weight(1f)
        ) {
            VoiceVisualizer(
                amplitudes = amplitudes,
                isActive = playbackState == PlaybackState.PLAYING,
                modifier = Modifier.fillMaxWidth().height(32.dp),
                style = VisualizerStyle(
                    barWidth = 3.dp,
                    barSpacing = 2.dp,
                    cornerRadius = 2.dp,
                    minBarHeight = 3.dp
                ),
                activeColor = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentPositionFormatted,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = durationFormatted,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Delete Button
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Recording",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
            )
        }
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
