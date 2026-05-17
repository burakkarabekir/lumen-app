package com.bksd.journal.presentation.journal.components

import androidx.compose.animation.core.FastOutSlowInEasing

internal object MomentCardDefaults {
    const val STRIP_WIDTH_DP = 5
    const val PANEL_EXPANDED_WIDTH_DP = 115
    const val ACTION_PANEL_WIDTH_DP = 72
    const val DRAG_THRESHOLD = 10f
    const val REVEAL_DURATION_MS = 450
    val RevealEasing = FastOutSlowInEasing
}
