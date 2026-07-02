package com.bksd.profile.presentation.di

import com.bksd.profile.presentation.EditProfileViewModel
import com.bksd.profile.presentation.ProfileViewModel
import com.bksd.profile.presentation.RemindersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profilePresentationModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::RemindersViewModel)
}
