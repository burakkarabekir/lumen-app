package com.bksd.core.design_system.component.button.fab

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
