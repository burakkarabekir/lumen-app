package com.bksd.journal.domain.di

import com.bksd.journal.domain.usecase.DeleteMomentUseCase
import com.bksd.journal.domain.usecase.GetMomentUseCase
import com.bksd.journal.domain.usecase.GetPagedMomentsUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import com.bksd.journal.domain.usecase.UpdateMomentUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val journalDomainModule = module {
    factoryOf(::GetPagedMomentsUseCase)
    factoryOf(::GetMomentUseCase)
    factoryOf(::DeleteMomentUseCase)
    factoryOf(::SyncMomentsUseCase)
    factoryOf(::UpdateMomentUseCase)
}
