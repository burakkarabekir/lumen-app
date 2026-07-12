package com.bksd.lumen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.LocalLanguageController
import com.bksd.core.design_system.theme.LocalThemeController
import com.bksd.core.design_system.theme.rememberAppThemeState
import com.bksd.core.domain.notification.ReminderScheduler
import com.bksd.core.domain.reminder.ReminderRepository
import com.bksd.lumen.language.AppEnvironment
import com.bksd.lumen.language.LanguageViewModel
import com.bksd.lumen.navigation.NavigationRoot
import com.bksd.lumen.theme.ThemeViewModel
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val languageViewModel = koinViewModel<LanguageViewModel>()
    val language by languageViewModel.language.collectAsState()

    val themeViewModel = koinViewModel<ThemeViewModel>()
    val mode by themeViewModel.themeMode.collectAsState()
    val themeState = rememberAppThemeState(mode = mode)

    val reminderScheduler = koinInject<ReminderScheduler>()
    val reminderRepository = koinInject<ReminderRepository>()
    var appliedLanguage by remember { mutableStateOf(language) }
    LaunchedEffect(language) {
        if (language != appliedLanguage) {
            appliedLanguage = language
            reminderScheduler.reschedule(reminderRepository.observe().first())
        }
    }

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