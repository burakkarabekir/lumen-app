package com.bksd.journal.presentation.di

import com.bksd.journal.presentation.detail.MomentDetailViewModel
import com.bksd.journal.presentation.journal.JournalViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val journalPresentationModule = module {
    viewModelOf(::JournalViewModel)
    viewModelOf(::MomentDetailViewModel)
}
