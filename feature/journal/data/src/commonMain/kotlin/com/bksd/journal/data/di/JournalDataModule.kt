package com.bksd.journal.data.di

import com.bksd.journal.data.remote.FirestoreMomentRepository
import com.bksd.journal.domain.repository.MomentRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val journalDataModule = module {
    singleOf(::FirestoreMomentRepository) bind MomentRepository::class
}
