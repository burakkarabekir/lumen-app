package com.bksd.moment.presentation.di

import com.bksd.core.presentation.util.ApplicationScope
import com.bksd.moment.presentation.create.CreateMomentViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val momentPresentationModule = module {
    single { ApplicationScope() }
    viewModelOf(::CreateMomentViewModel)
}
