package com.bksd.core.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Production entry point: accepts [AppThemeState] for full theme control.
 * Used at the app root where theme is resolved from [ThemeController].
 */
@Composable
fun AppTheme(
    themeState: AppThemeState,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (themeState.useDarkTheme) DarkColorScheme else LightColorScheme
    val extendedScheme = if (themeState.useDarkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

/**
 * Backward-compatible entry point for previews and simple usage.
 * Resolves dark/light from [darkTheme] boolean directly.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    AppTheme(
        themeState = AppThemeState(
            useDarkTheme = darkTheme,
        ),
        content = content,
    )
}