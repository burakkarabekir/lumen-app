package com.bksd.onboarding.presentation.di

import com.bksd.onboarding.presentation.OnboardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingPresentationModule = module {
    viewModelOf(::OnboardingViewModel)
}
