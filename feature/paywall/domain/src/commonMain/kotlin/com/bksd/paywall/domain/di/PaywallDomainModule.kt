package com.bksd.paywall.domain.di

import com.bksd.paywall.domain.usecase.GetPaywallConfigUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val paywallDomainModule = module {
    factoryOf(::GetPaywallConfigUseCase)
}
