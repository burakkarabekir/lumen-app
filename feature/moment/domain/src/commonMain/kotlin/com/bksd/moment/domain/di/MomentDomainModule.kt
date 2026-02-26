package com.bksd.moment.domain.di

import com.bksd.moment.domain.usecase.SaveMomentUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val momentDomainModule = module {
    factoryOf(::SaveMomentUseCase)
}
