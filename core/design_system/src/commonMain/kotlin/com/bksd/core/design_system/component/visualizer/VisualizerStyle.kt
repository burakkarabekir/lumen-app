package com.bksd.core.design_system.component.visualizer

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Style configuration for the [VoiceVisualizer] composable.
 * All dimensions are in Dp and will be converted to pixels at render time.
 */
@Immutable
data class VisualizerStyle(
    val barWidth: Dp = 3.dp,
    val barSpacing: Dp = 2.dp,
    val cornerRadius: Dp = 2.dp,
    val minBarHeight: Dp = 4.dp,
    val idleMinBarHeight: Dp = 2.dp
)

/**
 * Pixel-converted layout values used internally by the Canvas renderer.
 */
@Immutable
data class VisualizerDimensions(
    val barWidth: Float,
    val spacing: Float,
    val cornerRadius: Float,
    val minBarHeight: Float,
    val idleMinBarHeight: Float
)
