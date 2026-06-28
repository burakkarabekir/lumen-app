package com.bksd.profile.domain.di

import com.bksd.profile.domain.usecase.GetUserProfileUseCase
import com.bksd.profile.domain.usecase.SetProfileAvatarUseCase
import com.bksd.profile.domain.usecase.UpdateDisplayNameUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val profileDomainModule = module {
    factoryOf(::GetUserProfileUseCase)
    factoryOf(::SetProfileAvatarUseCase)
    factoryOf(::UpdateDisplayNameUseCase)
}
