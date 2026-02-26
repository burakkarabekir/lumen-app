package com.bksd.core.design_system.component.button.fab

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.Res
import com.bksd.core.design_system.add_new_entry
import com.bksd.core.design_system.cancel_recording
import com.bksd.core.design_system.ic_mic
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.buttonGradient
import com.bksd.core.design_system.theme.buttonGradientPressed
import com.bksd.core.design_system.theme.primary90
import com.bksd.core.design_system.theme.primary95
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.roundToInt

/**
 * Configuration for SpeakingFab behavior and appearance.
 *
 * @param cancelButtonOffset Horizontal offset for the cancel button from the FAB center
 * @param cancelThreshold Multiplier for the drag distance threshold to trigger cancellation (0.0-1.0)
 * @param cancelIconSize Size of the cancel icon
 * @param fabSize Size of the main FAB button
 */
data class SpeakingFabConfig(
    val cancelButtonOffset: Dp = (-100).dp,
    val cancelThreshold: Float = 0.8f,
    val cancelIconSize: Dp = 48.dp,
    val fabSize: Dp = 56.dp
)

/**
 * A floating action button with drag-to-cancel functionality for voice recording.
 *
 * Supports two interaction modes:
 * - Tap: Quick action via [onClick]
 * - Long press + drag: Quick recording with drag-to-cancel gesture
 *
 * When in recording mode ([isQuickRecording] = true), the user can drag the FAB to the left
 * to cancel the recording. Visual and haptic feedback is provided during the interaction.
 * The FAB animates smoothly back to its original position when released.
 *
 * @param isQuickRecording Whether quick recording mode is active
 * @param onClick Callback invoked on tap
 * @param onLongPressStart Callback invoked when long press starts
 * @param onLongPressEnd Callback invoked when long press ends with cancellation state
 * @param modifier Modifier to be applied to the container
 * @param config Configuration for the FAB behavior and appearance
 */
@Composable
fun SpeakingFab(
    isQuickRecording: Boolean,
    onClick: () -> Unit,
    onLongPressStart: () -> Unit,
    onLongPressEnd: (isCancelled: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    config: SpeakingFabConfig = SpeakingFabConfig()
) {

    val haptic = LocalHapticFeedback.current
    val cancelButtonOffset = config.cancelButtonOffset
    val cancelButtonOffsetPx = with(LocalDensity.current) {
        cancelButtonOffset.toPx()
    }
    var targetDragOffsetX by remember { mutableFloatStateOf(0f) }
    var needToHandleLongClickEnd by remember { mutableStateOf(false) }

    // Animated drag offset with spring animation for smooth return
    val animatedDragOffsetX by animateFloatAsState(
        targetValue = targetDragOffsetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "dragOffsetAnimation"
    )

    val isWithinCancelThreshold by remember(cancelButtonOffsetPx, config.cancelThreshold) {
        derivedStateOf {
            animatedDragOffsetX <= cancelButtonOffsetPx * config.cancelThreshold
        }
    }

    LaunchedEffect(isWithinCancelThreshold && isQuickRecording) {
        if (isWithinCancelThreshold) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val fabPositionOffset by remember {
        derivedStateOf {
            IntOffset(
                x = animatedDragOffsetX.toInt().coerceIn(
                    minimumValue = cancelButtonOffsetPx.roundToInt(),
                    maximumValue = 0
                ),
                y = 0
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() }
                )
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        needToHandleLongClickEnd = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongPressStart()
                    },
                    onDragEnd = {
                        if (needToHandleLongClickEnd) {
                            needToHandleLongClickEnd = false
                            onLongPressEnd(isWithinCancelThreshold)
                            targetDragOffsetX = 0f
                        }
                    },
                    onDragCancel = {
                        if (needToHandleLongClickEnd) {
                            needToHandleLongClickEnd = false
                            onLongPressEnd(isWithinCancelThreshold)
                            targetDragOffsetX = 0f
                        }
                    },
                    onDrag = { change, _ ->
                        targetDragOffsetX = (targetDragOffsetX + change.positionChange().x).coerceAtMost(0f)
                    }

                )
            }
            .semantics {
                stateDescription = if (isQuickRecording) "Recording" else "Ready"
                role = Role.Button
            }
    ) {
        if (isQuickRecording) {
            Box(
                modifier = Modifier
                    .offset(x = cancelButtonOffset)
                    .size(config.cancelIconSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.cancel_recording),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        SpeakingBubbleFab(
            showBubble = isQuickRecording,
            onClick = onClick,
            buttonSize = config.fabSize,
            icon = {
                Icon(
                    imageVector = if (isQuickRecording) {
                        vectorResource(Res.drawable.ic_mic)
                    } else Icons.Filled.Add,
                    contentDescription = stringResource(Res.string.add_new_entry),
                    tint = MaterialTheme.colorScheme.primary95,
                )
            },
            modifier = Modifier
                .offset { fabPositionOffset },
            colors = rememberBubbleFabColor(
                primary = if (isWithinCancelThreshold) {
                    SolidColor(MaterialTheme.colorScheme.error)
                } else MaterialTheme.colorScheme.buttonGradient,
                primaryPressed = MaterialTheme.colorScheme.buttonGradientPressed,
                outerCircle = if (isWithinCancelThreshold) {
                    SolidColor(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f))
                } else SolidColor(MaterialTheme.colorScheme.primary95),
                innerCircle = if (isWithinCancelThreshold) {
                    SolidColor(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                } else SolidColor(MaterialTheme.colorScheme.primary90),
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpeakingFabRecordingPreview() {
    AppTheme {
        SpeakingFab(
            isQuickRecording = true,
            onClick = {},
            onLongPressStart = {},
            onLongPressEnd = {},
            modifier = Modifier
                .fillMaxWidth()

        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SpeakingFabIdlePreview() {
    AppTheme {
        SpeakingFab(
            isQuickRecording = false,
            onClick = {},
            onLongPressStart = {},
            onLongPressEnd = {},
            modifier = Modifier
                .fillMaxWidth()

        )
    }
}