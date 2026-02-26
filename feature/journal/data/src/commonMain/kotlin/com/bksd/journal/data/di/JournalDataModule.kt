package com.bksd.journal.data.di

import com.bksd.journal.data.FakeMomentRepository
import com.bksd.journal.domain.repository.MomentRepository
import org.koin.dsl.module

val journalDataModule = module {
    single<MomentRepository> { FakeMomentRepository() }
}
