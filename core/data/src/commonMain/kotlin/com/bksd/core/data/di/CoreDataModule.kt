package com.bksd.core.data.di

import com.bksd.core.data.logging.KermitLogger
import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.data.remote.firebase.FirebaseStorageDataSource
import com.bksd.core.data.repository.MediaRepositoryImpl
import com.bksd.core.domain.logging.AppLogger
import com.bksd.core.domain.repository.MediaRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    includes(platformDataModule)

    single<AppLogger> { KermitLogger }
    singleOf(::FirebaseAuthDataSource)
    singleOf(::FirebaseStorageDataSource)
    singleOf(::MediaRepositoryImpl) bind MediaRepository::class
}