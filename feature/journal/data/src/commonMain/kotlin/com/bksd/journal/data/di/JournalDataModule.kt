package com.bksd.journal.data.di

import com.bksd.journal.data.MediatorMomentRepository
import com.bksd.journal.data.local.DomainToEntityMapper
import com.bksd.journal.data.local.EntityToDomainMapper
import com.bksd.journal.data.local.JournalDatabase
import com.bksd.journal.data.local.MomentDao
import com.bksd.journal.data.local.getJournalDatabaseBuilder
import com.bksd.journal.data.remote.FirestoreMomentRemoteDataSource
import com.bksd.journal.data.remote.MomentDtoMapper
import com.bksd.journal.data.remote.MomentToDtoMapper
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val journalDataModule = module {
    single<JournalDatabase> {
        getJournalDatabaseBuilder()
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single<MomentDao> { get<JournalDatabase>().momentDao() }

    singleOf(::FirestoreMomentRemoteDataSource)

    // Json instance for local entity serialization
    single { Json { ignoreUnknownKeys = true } }

    // Mappers: Dto ↔ Domain
    singleOf(::MomentDtoMapper)
    singleOf(::MomentToDtoMapper)

    // Mappers: Entity ↔ Domain
    singleOf(::EntityToDomainMapper)
    singleOf(::DomainToEntityMapper)

    singleOf(::MediatorMomentRepository) bind MomentRepository::class
}
