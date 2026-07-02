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
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.component.visualizer.VoiceVisualizer
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.cancel_recording
import com.bksd.moment.presentation.done
import com.bksd.moment.presentation.recording_in_progress
import com.bksd.moment.presentation.create.RecordingUiState
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

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
                .padding(horizontal = MaterialTheme.dimens.spacing.xxxl)
                .padding(bottom = MaterialTheme.dimens.spacing.huge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = stringResource(Res.string.recording_in_progress),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxxl))

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

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxl))

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
                    .height(MaterialTheme.dimens.size.topBar),
                style = VisualizerStyle(
                    barWidth = 4.dp,
                    barSpacing = 3.dp,
                    cornerRadius = 2.dp,
                    minBarHeight = 4.dp
                ),
                activeColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.huge))

            // Controls: Cancel — Pause indicator — Done
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel button
                FilledIconButton(
                    onClick = onCancelClick,
                    modifier = Modifier.size(MaterialTheme.dimens.size.fab),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.cancel_recording),
                        modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
                    )
                }

                // Done button
                FilledIconButton(
                    onClick = onDoneClick,
                    modifier = Modifier.size(MaterialTheme.dimens.size.fab),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(Res.string.done),
                        modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
                    )
                }
            }
        }
    }
}
