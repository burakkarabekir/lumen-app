package com.bksd.lumen.di

import com.bksd.auth.data.di.authDataModule
import com.bksd.auth.domain.di.authDomainModule
import com.bksd.auth.presentation.di.authPresentationModule
import com.bksd.core.data.di.coreDataModule
import com.bksd.insights.presentation.di.insightsPresentationModule
import com.bksd.journal.data.di.journalDataModule
import com.bksd.journal.domain.di.journalDomainModule
import com.bksd.journal.presentation.di.journalPresentationModule
import com.bksd.moment.domain.di.momentDomainModule
import com.bksd.moment.presentation.di.momentPresentationModule
import com.bksd.onboarding.presentation.di.onboardingPresentationModule
import com.bksd.paywall.presentation.di.paywallPresentationModule
import com.bksd.profile.presentation.di.profilePresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            coreDataModule,
            navigationModule,
            // Feature modules
            onboardingPresentationModule,
            authDataModule,
            authDomainModule,
            authPresentationModule,
            journalDataModule,
            journalDomainModule,
            journalPresentationModule,
            momentDomainModule,
            momentPresentationModule,
            insightsPresentationModule,
            profilePresentationModule,
            paywallPresentationModule
        )
    }
}