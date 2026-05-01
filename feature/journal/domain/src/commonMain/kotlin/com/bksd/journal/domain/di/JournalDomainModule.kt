package com.bksd.journal.domain.di

import com.bksd.journal.domain.usecase.GetMomentUseCase
import com.bksd.journal.domain.usecase.GetMomentsByDateUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val journalDomainModule = module {
    factoryOf(::GetMomentsByDateUseCase)
    factoryOf(::GetMomentUseCase)
    factoryOf(::SyncMomentsUseCase)
}
