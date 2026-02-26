package com.bksd.lumen.di

import com.bksd.core.domain.logging.AppLogger
import com.bksd.lumen.navigation.NavigationState
import com.bksd.lumen.navigation.Navigator
import org.koin.dsl.module

val navigationModule = module {
    factory { (navigationState: NavigationState) ->
        Navigator(
            state = navigationState,
             logger = get<AppLogger>()
        )
    }
}

