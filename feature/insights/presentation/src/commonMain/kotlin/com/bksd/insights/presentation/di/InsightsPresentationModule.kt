package com.bksd.insights.presentation.di

import com.bksd.insights.presentation.InsightsViewModel
import com.bksd.insights.presentation.places.PlacesViewModel
import com.bksd.insights.presentation.reflection.ReflectionViewModel
import com.bksd.insights.presentation.reflection.full.WeeklyReflectionDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val insightsPresentationModule = module {
    viewModelOf(::InsightsViewModel)
    viewModelOf(::PlacesViewModel)
    viewModelOf(::ReflectionViewModel)
    viewModelOf(::WeeklyReflectionDetailViewModel)
}
