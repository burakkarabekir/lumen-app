package com.bksd.auth.data.di

import com.bksd.auth.data.AuthRepositoryImpl
import com.bksd.auth.domain.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
}
