package com.bksd.paywall.presentation.di

import com.bksd.paywall.presentation.PaywallViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val paywallPresentationModule = module {
    viewModelOf(::PaywallViewModel)
}
