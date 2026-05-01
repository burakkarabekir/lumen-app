package com.bksd.core.data.di

import com.bksd.core.data.logging.KermitLogger
import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.data.remote.firebase.FirebaseFirestoreDataSource
import com.bksd.core.data.remote.firebase.FirebaseStorageDataSource
import com.bksd.core.data.repository.MediaRepositoryImpl
import com.bksd.core.data.storage.FirebaseSessionStorage
import com.bksd.core.data.theme.ThemeRepositoryImpl
import com.bksd.core.domain.logging.AppLogger
import com.bksd.core.domain.repository.MediaRepository
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.domain.theme.GetAppThemeUseCase
import com.bksd.core.domain.theme.SetAppThemeUseCase
import com.bksd.core.domain.theme.ThemeRepository
import kotlinx.datetime.TimeZone
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.time.Clock

val coreDataModule = module {
    includes(platformDataModule)

    single<Clock> { Clock.System }
    single { TimeZone.currentSystemDefault() }
    single<AppLogger> { KermitLogger }
    singleOf(::FirebaseAuthDataSource)
    singleOf(::FirebaseFirestoreDataSource)
    singleOf(::FirebaseStorageDataSource)
    singleOf(::MediaRepositoryImpl) bind MediaRepository::class
    singleOf(::FirebaseSessionStorage) bind SessionStorage::class

    singleOf(::ThemeRepositoryImpl) bind ThemeRepository::class
    factoryOf(::GetAppThemeUseCase)
    factoryOf(::SetAppThemeUseCase)
}