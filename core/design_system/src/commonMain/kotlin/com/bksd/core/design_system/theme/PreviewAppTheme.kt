package com.bksd.core.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.bksd.core.domain.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Preview-safe theme wrapper. Provides a no-op [ThemeController] via [LocalThemeController]
 * so previews do not require DI or real ViewModels.
 */
@Composable
fun PreviewAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val previewController = object : ThemeController {
        override val themeMode: StateFlow<AppThemeMode> =
            MutableStateFlow(if (darkTheme) AppThemeMode.DARK else AppThemeMode.LIGHT)

        override fun setTheme(mode: AppThemeMode) {
            // No-op in preview
        }
    }

    CompositionLocalProvider(LocalThemeController provides previewController) {
        AppTheme(darkTheme = darkTheme, content = content)
    }
}
