package com.bksd.profile.domain.di

import com.bksd.profile.domain.usecase.ClearUserDataUseCase
import com.bksd.profile.domain.usecase.GetProfileAvatarUseCase
import com.bksd.profile.domain.usecase.SetProfileAvatarUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileDomainModule = module {
    factoryOf(::GetProfileAvatarUseCase)
    factoryOf(::SetProfileAvatarUseCase)
    factoryOf(::ClearUserDataUseCase)
}
