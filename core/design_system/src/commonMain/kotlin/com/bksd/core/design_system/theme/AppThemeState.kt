package com.bksd.core.design_system.theme

import androidx.compose.runtime.Immutable
import com.bksd.core.domain.theme.AppThemeMode

/**
 * Resolved, immutable theme state for UI consumption.
 * Only [mode] is persisted. [useDarkTheme] is resolved at the root.
 * [useDynamicColor] is future-ready for Material You / dynamic color.
 */
@Immutable
data class AppThemeState(
    val mode: AppThemeMode = AppThemeMode.SYSTEM,
    val useDarkTheme: Boolean = false,
    val useDynamicColor: Boolean = false,
)
