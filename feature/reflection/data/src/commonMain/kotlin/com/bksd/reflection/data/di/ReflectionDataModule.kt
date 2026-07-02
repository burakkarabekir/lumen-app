package com.bksd.reflection.data.di

import com.bksd.reflection.data.local.DataStoreMomentAnalysisStore
import com.bksd.reflection.data.local.DataStoreWeeklyReflectionStore
import com.bksd.reflection.data.remote.SupabaseEntryReflector
import com.bksd.reflection.data.remote.SupabaseWeeklyReflector
import com.bksd.reflection.domain.analysis.EntryReflector
import com.bksd.reflection.domain.analysis.WeeklyReflector
import com.bksd.reflection.domain.repository.MomentAnalysisStore
import com.bksd.reflection.domain.repository.WeeklyReflectionStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val reflectionDataModule = module {
    singleOf(::SupabaseEntryReflector) bind EntryReflector::class
    singleOf(::SupabaseWeeklyReflector) bind WeeklyReflector::class
    single<WeeklyReflectionStore> { DataStoreWeeklyReflectionStore(get()) }
    single<MomentAnalysisStore> { DataStoreMomentAnalysisStore(get(), get()) }
}
