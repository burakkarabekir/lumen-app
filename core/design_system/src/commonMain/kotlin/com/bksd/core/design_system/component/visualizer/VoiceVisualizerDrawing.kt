package com.bksd.core.design_system.component.visualizer

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.PI
import kotlin.math.sin

/**
 * Draws amplitude bars from real data, centered horizontally.
 */
internal fun DrawScope.drawActiveVoiceLevels(
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
internal fun DrawScope.drawIdleVoiceLevels(
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
