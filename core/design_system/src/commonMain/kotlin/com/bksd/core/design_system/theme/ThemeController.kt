package com.bksd.core.design_system.theme

import com.bksd.core.domain.theme.AppThemeMode
import kotlinx.coroutines.flow.StateFlow

/**
 * Controller contract for theme management.
 * Provided via [LocalThemeController] CompositionLocal.
 * Implemented by ThemeViewModel at the app root.
 */
interface ThemeController {
    val themeMode: StateFlow<AppThemeMode>
    fun setTheme(mode: AppThemeMode)
}
