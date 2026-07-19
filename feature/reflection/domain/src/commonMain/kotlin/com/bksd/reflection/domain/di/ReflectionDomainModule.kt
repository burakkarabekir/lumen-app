package com.bksd.reflection.domain.di

import com.bksd.reflection.domain.usecase.AnalyzeAndReflectUseCase
import com.bksd.reflection.domain.usecase.BuildWeeklyInsightsUseCase
import com.bksd.reflection.domain.usecase.GenerateWeeklyReflectionUseCase
import com.bksd.reflection.domain.usecase.ObserveEntryAnalysisUseCase
import com.bksd.reflection.domain.usecase.ObserveWeeklyReflectionUseCase
import com.bksd.reflection.domain.usecase.RequestEntryAnalysisUseCase
import com.bksd.reflection.domain.usecase.SyncReflectionsUseCase
import com.bksd.reflection.domain.usecase.TrendSummaryBuilder
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val reflectionDomainModule = module {
    factoryOf(::TrendSummaryBuilder)
    factoryOf(::AnalyzeAndReflectUseCase)
    factoryOf(::RequestEntryAnalysisUseCase)
    factoryOf(::SyncReflectionsUseCase)
    factoryOf(::ObserveEntryAnalysisUseCase)
    factoryOf(::GenerateWeeklyReflectionUseCase)
    factoryOf(::ObserveWeeklyReflectionUseCase)
    factoryOf(::BuildWeeklyInsightsUseCase)
}
