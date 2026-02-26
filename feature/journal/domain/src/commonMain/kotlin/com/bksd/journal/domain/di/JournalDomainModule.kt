package com.bksd.journal.domain.di

import com.bksd.journal.domain.usecase.GetMomentUseCase
import com.bksd.journal.domain.usecase.GetMomentsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val journalDomainModule = module {
    factoryOf(::GetMomentsUseCase)
    factoryOf(::GetMomentUseCase)
}
