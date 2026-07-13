package com.bksd.onboarding.data.di

import com.bksd.onboarding.data.repository.OnboardingRepositoryImpl
import com.bksd.onboarding.domain.repository.OnboardingRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val onboardingDataModule = module {
    singleOf(::OnboardingRepositoryImpl) bind OnboardingRepository::class
}
