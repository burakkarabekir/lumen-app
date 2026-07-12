package com.bksd.lumen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.LocalLanguageController
import com.bksd.core.design_system.theme.LocalThemeController
import com.bksd.core.design_system.theme.rememberAppThemeState
import com.bksd.lumen.language.AppEnvironment
import com.bksd.lumen.language.LanguageViewModel
import com.bksd.lumen.navigation.NavigationRoot
import com.bksd.lumen.theme.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val languageViewModel = koinViewModel<LanguageViewModel>()
    val language by languageViewModel.language.collectAsState()

    val themeViewModel = koinViewModel<ThemeViewModel>()
    val mode by themeViewModel.themeMode.collectAsState()
    val themeState = rememberAppThemeState(mode = mode)

    AppEnvironment(localeTag = language.tag) {
        CompositionLocalProvider(
            LocalThemeController provides themeViewModel,
            LocalLanguageController provides languageViewModel,
        ) {
            AppTheme(themeState = themeState) {
                SystemBarsAppearance(darkTheme = themeState.useDarkTheme)
                NavigationRoot()
            }
        }
    }
}