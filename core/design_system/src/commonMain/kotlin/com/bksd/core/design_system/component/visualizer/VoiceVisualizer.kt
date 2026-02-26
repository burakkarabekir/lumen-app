package com.bksd.core.design_system.component.visualizer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.math.PI
import kotlin.math.sin

/**
 * Remembers pixel-converted dimensions from a [VisualizerStyle].
 */
@Composable
internal fun rememberVisualizerDimensions(
    style: VisualizerStyle
): VisualizerDimensions {
    val density = LocalDensity.current
    return remember(density, style) {
        with(density) {
            VisualizerDimensions(
                barWidth = style.barWidth.toPx(),
                spacing = style.barSpacing.toPx(),
                cornerRadius = style.cornerRadius.toPx(),
                minBarHeight = style.minBarHeight.toPx(),
                idleMinBarHeight = style.idleMinBarHeight.toPx()
            )
        }
    }
}

/**
 * Canvas-based voice level visualizer.
 *
 * Renders amplitude bars centered vertically. When [isActive] is true and
 * [amplitudes] is non-empty, bars reflect real amplitude data using [activeColor].
 * When idle, a gentle sine-wave animation pulses with [idleColor].
 *
 * @param amplitudes Normalized amplitude samples (0f..1f)
 * @param isActive Whether the visualizer is in an active state (recording or playing)
 * @param modifier Layout modifier
 * @param style Visual style configuration
 * @param activeColor Color for bars during active state
 * @param idleColor Color for bars during idle state
 */
@Composable
fun VoiceVisualizer(
    amplitudes: ImmutableList<Float>,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    style: VisualizerStyle = VisualizerStyle(),
    activeColor: Color = MaterialTheme.colorScheme.primary,
    idleColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
) {
    val dimensions = rememberVisualizerDimensions(style)

    // Idle animation: gentle sine-wave pulse
    val transition = rememberInfiniteTransition(label = "voiceLevelAnimation")
    val idleAnimationProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "idleVoiceLevels"
    )

    Canvas(modifier = modifier) {
        val totalBarWidth = dimensions.barWidth + dimensions.spacing
        val barCount = (size.width / totalBarWidth).toInt()
        val centerY = size.height / 2

        if (barCount <= 0) return@Canvas

        if (isActive && amplitudes.isNotEmpty()) {
            drawActiveVoiceLevels(
                amplitudes = amplitudes,
                barCount = barCount,
                dimensions = dimensions,
                centerY = centerY,
                color = activeColor
            )
        } else {
            drawIdleVoiceLevels(
                barCount = barCount,
                dimensions = dimensions,
                centerY = centerY,
                color = idleColor,
                animationProgress = idleAnimationProgress
            )
        }
    }
}

/**
 * Draws amplitude bars from real data, centered horizontally.
 */
private fun DrawScope.drawActiveVoiceLevels(
    amplitudes: List<Float>,
    barCount: Int,
    dimensions: VisualizerDimensions,
    centerY: Float,
    color: Color
) {
    val contentWidth = barCount * (dimensions.barWidth + dimensions.spacing) - dimensions.spacing
    val startX = (size.width - contentWidth) / 2

    for (i in 0 until barCount) {
        val ratio = i.toFloat() / barCount
        val amplitudeIndex = (ratio * amplitudes.size).toInt().coerceIn(0, amplitudes.lastIndex)
        val amplitude = amplitudes.getOrElse(amplitudeIndex) { 0f }
        val barHeight = (amplitude * size.height).coerceIn(dimensions.minBarHeight, size.height)
        val x = startX + i * (dimensions.barWidth + dimensions.spacing)

        drawRoundRect(
            color = color,
            topLeft = Offset(x, centerY - barHeight / 2),
            size = Size(dimensions.barWidth, barHeight),
            cornerRadius = CornerRadius(dimensions.cornerRadius)
        )
    }
}

/**
 * Draws a gentle sine-wave idle animation.
 */
private fun DrawScope.drawIdleVoiceLevels(
    barCount: Int,
    dimensions: VisualizerDimensions,
    centerY: Float,
    color: Color,
    animationProgress: Float
) {
    val contentWidth = barCount * (dimensions.barWidth + dimensions.spacing) - dimensions.spacing
    val startX = (size.width - contentWidth) / 2

    for (i in 0 until barCount) {
        val phase =
            (i.toFloat() / barCount) * 2 * PI.toFloat() + animationProgress * 2 * PI.toFloat()
        val sineValue = (sin(phase) + 1f) / 2f // Normalize to 0..1
        val barHeight = (sineValue * size.height * 0.3f).coerceAtLeast(dimensions.idleMinBarHeight)
        val x = startX + i * (dimensions.barWidth + dimensions.spacing)

        drawRoundRect(
            color = color,
            topLeft = Offset(x, centerY - barHeight / 2),
            size = Size(dimensions.barWidth, barHeight),
            cornerRadius = CornerRadius(dimensions.cornerRadius)
        )
    }
}

// ==================== Previews ====================

@Preview
@Composable
private fun PreviewRecording() {
    AppTheme {
        VoiceVisualizer(
            amplitudes = persistentListOf(
                0.2f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f,
                0.1f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f
            ),
            isActive = true,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewIdle() {
    AppTheme {
        VoiceVisualizer(
            amplitudes = persistentListOf(),
            isActive = false,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewDarkRecording() {
    AppTheme(darkTheme = true) {
        VoiceVisualizer(
            amplitudes = persistentListOf(
                0.2f, 0.5f, 0.8f, 0.3f, 0.6f, 0.9f, 0.4f, 0.7f
            ),
            isActive = true,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        )
    }
}
