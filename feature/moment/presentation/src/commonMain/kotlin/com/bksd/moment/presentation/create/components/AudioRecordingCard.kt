package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.visualizer.VisualizerStyle
import com.bksd.core.design_system.component.visualizer.VoiceVisualizer
import com.bksd.moment.presentation.create.RecordingUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AudioRecordingCard(
    recordingState: RecordingUiState.Recording,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Stop Button
        IconButton(
            onClick = onStopClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.error)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Stop Recording",
                tint = MaterialTheme.colorScheme.onError
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Waveform Visualizer — Canvas-based
        VoiceVisualizer(
            amplitudes = recordingState.amplitudes.toImmutableList(),
            isActive = true,
            modifier = Modifier.weight(1f).height(30.dp),
            style = VisualizerStyle(
                barWidth = 3.dp,
                barSpacing = 2.dp,
                cornerRadius = 2.dp,
                minBarHeight = 4.dp
            ),
            activeColor = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Timer
        Text(
            text = recordingState.elapsedFormatted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}
