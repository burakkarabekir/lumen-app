package com.bksd.lumen.di

import com.bksd.lumen.main.MainViewModel
import com.bksd.lumen.theme.ThemeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::ThemeViewModel)
    viewModelOf(::MainViewModel)
}
