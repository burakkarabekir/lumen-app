package com.bksd.lumen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.LocalThemeController
import com.bksd.core.design_system.theme.rememberAppThemeState
import com.bksd.lumen.navigation.NavigationRoot
import com.bksd.lumen.theme.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val mode by themeViewModel.themeMode.collectAsState()
    val themeState = rememberAppThemeState(mode = mode)

    CompositionLocalProvider(LocalThemeController provides themeViewModel) {
        AppTheme(themeState = themeState) {
            NavigationRoot()
        }
    }
}