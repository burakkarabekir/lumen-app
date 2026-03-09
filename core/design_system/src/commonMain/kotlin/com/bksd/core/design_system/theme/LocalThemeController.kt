package com.bksd.core.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.bksd.core.domain.theme.AppThemeMode

/**
 * CompositionLocal providing [ThemeController] to the UI tree.
 * Must be provided at the app root via CompositionLocalProvider.
 */
val LocalThemeController = staticCompositionLocalOf<ThemeController> {
    error("ThemeController not provided. Ensure LocalThemeController is provided at the app root.")
}

/**
 * Convenience accessor for the current [ThemeController].
 */
@Composable
fun rememberThemeController(): ThemeController = LocalThemeController.current

/**
 * Resolves [AppThemeMode] into a fully materialized [AppThemeState].
 * System dark/light is resolved here at the Compose root — never inside ViewModel or data layer.
 */
@Composable
fun rememberAppThemeState(
    mode: AppThemeMode,
    systemDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false,
): AppThemeState {
    return remember(mode, systemDarkTheme, useDynamicColor) {
        AppThemeState(
            mode = mode,
            useDarkTheme = when (mode) {
                AppThemeMode.SYSTEM -> systemDarkTheme
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
            },
            useDynamicColor = useDynamicColor,
        )
    }
}
