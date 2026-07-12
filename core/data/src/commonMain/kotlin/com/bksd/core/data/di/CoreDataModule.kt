package com.bksd.core.data.di

import com.bksd.core.data.applock.AppLockRepositoryImpl
import com.bksd.core.data.logging.KermitLogger
import com.bksd.core.data.reminder.ReminderRepositoryImpl
import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.data.remote.supabase.SupabaseConsentRepository
import com.bksd.core.data.remote.supabase.SupabasePremiumStatusSource
import com.bksd.core.data.remote.supabase.SupabaseStorageDataSource
import com.bksd.core.data.remote.supabase.createLumenSupabaseClient
import com.bksd.core.data.repository.MediaRepositoryImpl
import com.bksd.core.data.storage.SupabaseSessionStorage
import com.bksd.core.data.theme.ThemeRepositoryImpl
import com.bksd.core.domain.applock.AppLockRepository
import com.bksd.core.domain.billing.PremiumStatusSource
import com.bksd.core.domain.cleanup.LocalDataCleaner
import com.bksd.core.domain.consent.ConsentRepository
import com.bksd.core.domain.logging.AppLogger
import com.bksd.core.domain.reminder.ReminderRepository
import com.bksd.core.domain.repository.MediaRepository
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.domain.theme.GetAppThemeUseCase
import com.bksd.core.domain.theme.SetAppThemeUseCase
import com.bksd.core.domain.theme.ThemeRepository
import kotlinx.datetime.TimeZone
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import kotlin.time.Clock

val coreDataModule = module {
    includes(platformDataModule)

    single<Clock> { Clock.System }
    single { TimeZone.currentSystemDefault() }
    single<AppLogger> { KermitLogger }
    single { createLumenSupabaseClient() }
    singleOf(::SupabaseAuthDataSource)
    singleOf(::SupabaseStorageDataSource)
    singleOf(::SupabasePremiumStatusSource) bind PremiumStatusSource::class
    singleOf(::SupabaseConsentRepository) bind ConsentRepository::class
    singleOf(::MediaRepositoryImpl) bind MediaRepository::class
    singleOf(::SupabaseSessionStorage) bind SessionStorage::class

    singleOf(::ThemeRepositoryImpl) bind ThemeRepository::class
    factoryOf(::GetAppThemeUseCase)
    factoryOf(::SetAppThemeUseCase)

    singleOf(::ReminderRepositoryImpl) bind ReminderRepository::class
    singleOf(::AppLockRepositoryImpl) binds arrayOf(
        AppLockRepository::class,
        LocalDataCleaner::class,
    )
}