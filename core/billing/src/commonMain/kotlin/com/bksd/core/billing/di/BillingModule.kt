package com.bksd.core.billing.di

import com.bksd.core.billing.RevenueCatEntitlementRepository
import com.bksd.core.domain.billing.EntitlementRepository
import com.bksd.core.domain.billing.ObserveEntitlementUseCase
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val billingModule: Module = module {
    single { RevenueCatEntitlementRepository(get()) } bind EntitlementRepository::class
    factory { ObserveEntitlementUseCase(get()) }
}
