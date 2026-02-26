package com.bksd.core.design_system.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Lumen spacing tokens for consistent layout across the app.
 * Based on 4dp grid system with 8dp corner radius (Stitch ROUND_EIGHT).
 */
object LumenSpacing {
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 24.dp
    val xxxl: Dp = 32.dp
    val huge: Dp = 48.dp
    val massive: Dp = 64.dp
}

/**
 * Lumen corner radius tokens.
 * Default roundness from Stitch: ROUND_EIGHT (8dp).
 */
object LumenRadius {
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp   // Default from Stitch design
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 24.dp
    val full: Dp = 100.dp
}
