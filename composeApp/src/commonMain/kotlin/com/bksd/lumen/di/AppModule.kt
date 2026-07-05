package com.bksd.lumen.di

import com.bksd.lumen.consent.ConsentGateViewModel
import com.bksd.lumen.lock.LockGateViewModel
import com.bksd.lumen.main.MainViewModel
import com.bksd.lumen.theme.ThemeViewModel
import com.bksd.lumen.welcome.LoginWelcomeSignal
import com.bksd.lumen.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { LoginWelcomeSignal() }
    viewModelOf(::ThemeViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::ConsentGateViewModel)
    viewModelOf(::LockGateViewModel)
}
