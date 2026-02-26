package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.visualizer.VisualizerStyle
import com.bksd.core.design_system.component.visualizer.VoiceVisualizer
import com.bksd.moment.presentation.create.RecordingUiState
import kotlinx.collections.immutable.persistentListOf

/**
 * Modal bottom sheet for voice recording.
 * Opens when the mic button is tapped, recording starts immediately.
 * User can confirm (Done) or cancel the recording.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecordingBottomSheet(
    recordingState: RecordingUiState,
    onDoneClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Recording your memories...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Timer
            val elapsed = when (recordingState) {
                is RecordingUiState.Recording -> recordingState.elapsedFormatted
                else -> "00:00"
            }
            Text(
                text = elapsed,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Visualizer
            val amplitudes = when (recordingState) {
                is RecordingUiState.Recording -> recordingState.amplitudes
                else -> persistentListOf()
            }
            VoiceVisualizer(
                amplitudes = amplitudes,
                isActive = recordingState is RecordingUiState.Recording,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                style = VisualizerStyle(
                    barWidth = 4.dp,
                    barSpacing = 3.dp,
                    cornerRadius = 2.dp,
                    minBarHeight = 4.dp
                ),
                activeColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Controls: Cancel — Pause indicator — Done
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel button
                FilledIconButton(
                    onClick = onCancelClick,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel Recording",
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Done button
                FilledIconButton(
                    onClick = onDoneClick,
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
