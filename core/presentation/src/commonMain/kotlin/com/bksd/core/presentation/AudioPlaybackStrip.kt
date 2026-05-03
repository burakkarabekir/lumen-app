package com.bksd.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.visualizer.VisualizerStyle
import com.bksd.core.design_system.component.visualizer.VoiceVisualizer
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.model.PlaybackState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AudioPlaybackStrip(
    playbackState: PlaybackState,
    amplitudes: ImmutableList<Float>,
    durationFormatted: String,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    mode: AudioPlaybackMode = AudioPlaybackMode.STANDARD,
    currentPositionFormatted: String,
) {
    when (mode) {
        AudioPlaybackMode.STANDARD -> StandardStrip(
            playbackState = playbackState,
            amplitudes = amplitudes,
            currentPositionFormatted = currentPositionFormatted,
            durationFormatted = durationFormatted,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            modifier = modifier
        )

        AudioPlaybackMode.COMPACT -> CompactStrip(
            playbackState = playbackState,
            currentPositionFormatted = currentPositionFormatted,
            durationFormatted = durationFormatted,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            modifier = modifier
        )
    }
}

// ────────────────────────── Standard ──────────────────────────

@Composable
private fun StandardStrip(
    playbackState: PlaybackState,
    amplitudes: ImmutableList<Float>,
    currentPositionFormatted: String,
    durationFormatted: String,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            VoiceVisualizer(
                amplitudes = amplitudes,
                isActive = playbackState == PlaybackState.PLAYING,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                style = StandardVisualizerStyle,
                activeColor = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
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
    }
}

// ────────────────────────── Compact ──────────────────────────

@Composable
private fun CompactStrip(
    playbackState: PlaybackState,
    currentPositionFormatted: String,
    durationFormatted: String,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AudioPlaybackButton(
            playbackState = playbackState,
            onPlayClick = onPlayClick,
            onPauseClick = onPauseClick,
            modifier = Modifier.size(36.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                contentColor = MaterialTheme.colorScheme.primary
            )
        )

        val displayText = if (playbackState == PlaybackState.STOPPED) {
            durationFormatted
        } else {
            currentPositionFormatted
        }

        Box(
            modifier = Modifier.padding(end = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // Invisible text to maintain fixed max width of the container
            Text(
                text = durationFormatted,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Transparent
            )
            // Visible text
            Text(
                text = displayText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        AudioPlaybackStrip(
            PlaybackState.PLAYING,
            amplitudes = persistentListOf(),
            mode = AudioPlaybackMode.COMPACT,
            currentPositionFormatted = "1:45",
            durationFormatted = "5:00",
            onPlayClick = {},
            onPauseClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AudioPlaybackStrip(
            PlaybackState.STOPPED,
            amplitudes = persistentListOf(),
            currentPositionFormatted = "1:45",
            durationFormatted = "5:00",
            onPlayClick = {},
            onPauseClick = {}
        )
    }
}

// ────────────────────────── Styles ──────────────────────────

private val StandardVisualizerStyle = VisualizerStyle(
    barWidth = 3.dp,
    barSpacing = 2.dp,
    cornerRadius = 2.dp,
    minBarHeight = 3.dp
)
