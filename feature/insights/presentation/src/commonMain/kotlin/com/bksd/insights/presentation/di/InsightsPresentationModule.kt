package com.bksd.insights.presentation.di

import com.bksd.insights.presentation.InsightsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val insightsPresentationModule = module {
    viewModelOf(::InsightsViewModel)
}
