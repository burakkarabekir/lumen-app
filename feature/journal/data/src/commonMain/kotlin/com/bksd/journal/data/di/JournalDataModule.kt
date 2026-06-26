package com.bksd.journal.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.bksd.core.domain.cleanup.LocalDataCleaner
import com.bksd.core.domain.repository.MomentRepository
import com.bksd.journal.data.MediatorMomentRepository
import com.bksd.journal.data.local.DomainToEntityMapper
import com.bksd.journal.data.local.EntityToDomainMapper
import com.bksd.journal.data.local.JournalDatabase
import com.bksd.journal.data.local.MomentDao
import com.bksd.journal.data.local.getJournalDatabaseBuilder
import com.bksd.journal.data.remote.MomentDtoMapper
import com.bksd.journal.data.remote.MomentToDtoMapper
import com.bksd.journal.data.remote.SupabaseMomentRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val journalDataModule = module {
    single<JournalDatabase> {
        getJournalDatabaseBuilder()
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<MomentDao> { get<JournalDatabase>().momentDao() }

    singleOf(::SupabaseMomentRemoteDataSource)

    // Json instance for local entity serialization
    single { Json { ignoreUnknownKeys = true } }

    // Mappers: Dto ↔ Domain
    singleOf(::MomentDtoMapper)
    singleOf(::MomentToDtoMapper)

    // Mappers: Entity ↔ Domain
    singleOf(::EntityToDomainMapper)
    singleOf(::DomainToEntityMapper)

    singleOf(::MediatorMomentRepository) binds arrayOf(
        MomentRepository::class,
        LocalDataCleaner::class
    )
}
