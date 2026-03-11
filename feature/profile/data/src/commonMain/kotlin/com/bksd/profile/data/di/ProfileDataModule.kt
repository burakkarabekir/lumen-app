package com.bksd.profile.data.di

import com.bksd.profile.data.repository.ProfileRepositoryImpl
import com.bksd.profile.domain.repository.ProfileRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDataModule = module {
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
}
