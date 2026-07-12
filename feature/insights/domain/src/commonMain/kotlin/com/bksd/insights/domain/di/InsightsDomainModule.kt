package com.bksd.insights.domain.di

import com.bksd.insights.domain.calculator.InsightsCalculator
import com.bksd.insights.domain.usecase.ComputeInsightsUseCase
import com.bksd.insights.domain.usecase.GetPlacesUseCase
import com.bksd.insights.domain.usecase.ObserveAllMomentsUseCase
import com.bksd.insights.domain.usecase.SyncAllMomentsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val insightsDomainModule = module {
    factoryOf(::InsightsCalculator)
    factoryOf(::ObserveAllMomentsUseCase)
    factoryOf(::SyncAllMomentsUseCase)
    factoryOf(::ComputeInsightsUseCase)
    factoryOf(::GetPlacesUseCase)
}
