package com.bksd.auth.presentation.di

import com.bksd.auth.presentation.resetpassword.ResetPasswordViewModel
import com.bksd.auth.presentation.signin.SignInViewModel
import com.bksd.auth.presentation.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ResetPasswordViewModel)
}
