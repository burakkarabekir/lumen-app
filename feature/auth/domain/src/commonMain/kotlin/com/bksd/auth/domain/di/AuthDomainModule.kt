package com.bksd.auth.domain.di

import com.bksd.auth.domain.usecase.ResetPasswordUseCase
import com.bksd.auth.domain.usecase.SignInUseCase
import com.bksd.auth.domain.usecase.SignInWithAppleUseCase
import com.bksd.auth.domain.usecase.SignInWithGoogleUseCase
import com.bksd.auth.domain.usecase.SignOutUseCase
import com.bksd.auth.domain.usecase.SignUpUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authDomainModule = module {
    factoryOf(::SignInUseCase)
    factoryOf(::SignUpUseCase)
    factoryOf(::SignOutUseCase)
    factoryOf(::ResetPasswordUseCase)
    factoryOf(::SignInWithGoogleUseCase)
    factoryOf(::SignInWithAppleUseCase)
}
