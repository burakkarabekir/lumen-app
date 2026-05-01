package com.bksd.journal.presentation.di

import com.bksd.journal.presentation.detail.MomentDetailViewModel
import com.bksd.journal.presentation.journal.JournalViewModel
import com.bksd.journal.presentation.util.DefaultMomentFormatter
import com.bksd.journal.presentation.util.MomentFormatter
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val journalPresentationModule = module {
    singleOf(::DefaultMomentFormatter) bind MomentFormatter::class
    viewModelOf(::JournalViewModel)
    viewModelOf(::MomentDetailViewModel)
}
