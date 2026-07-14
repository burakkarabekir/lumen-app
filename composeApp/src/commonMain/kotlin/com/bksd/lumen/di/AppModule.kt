package com.bksd.lumen.di

import com.bksd.core.domain.notification.ReminderTextProvider
import com.bksd.core.presentation.notification.ComposeReminderTextProvider
import com.bksd.core.presentation.snackbar.SnackbarController
import com.bksd.lumen.consent.ConsentGateViewModel
import com.bksd.lumen.lock.LockGateViewModel
import com.bksd.lumen.language.LanguageViewModel
import com.bksd.lumen.main.MainViewModel
import com.bksd.lumen.reminder.ReminderLaunchSignal
import com.bksd.lumen.theme.ThemeViewModel
import com.bksd.lumen.welcome.LoginWelcomeSignal
import com.bksd.lumen.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { LoginWelcomeSignal() }
    single { ReminderLaunchSignal() }
    single { SnackbarController() }
    single<ReminderTextProvider> { ComposeReminderTextProvider() }
    viewModelOf(::ThemeViewModel)
    viewModelOf(::LanguageViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::ConsentGateViewModel)
    viewModelOf(::LockGateViewModel)
}
