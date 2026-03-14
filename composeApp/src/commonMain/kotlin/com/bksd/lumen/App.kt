package com.bksd.lumen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.LocalThemeController
import com.bksd.core.design_system.theme.rememberAppThemeState
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.lumen.main.MainEvent
import com.bksd.lumen.main.MainViewModel
import com.bksd.lumen.navigation.NavigationRoot
import com.bksd.lumen.navigation.Navigator
import com.bksd.lumen.navigation.TOP_LEVEL_DESTINATIONS
import com.bksd.lumen.navigation.rememberNavigationState
import com.bksd.lumen.navigation.route.Route
import com.bksd.lumen.theme.ThemeViewModel
import kotlinx.collections.immutable.toImmutableSet
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun App() {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val mode by themeViewModel.themeMode.collectAsState()
    val themeState = rememberAppThemeState(mode = mode)

    val mainViewModel = koinViewModel<MainViewModel>()
    val mainState by mainViewModel.state.collectAsState()

    CompositionLocalProvider(LocalThemeController provides themeViewModel) {
        AppTheme(themeState = themeState) {
            if (!mainState.isCheckingAuth) {
                val startDestination = remember(mainState.isLoggedIn) {
                    if (mainState.isLoggedIn) Route.Main.Journal else Route.Auth.SignIn
                }

                val navigationState = rememberNavigationState(
                    startRoute = startDestination,
                    topLevelRoutes = remember(startDestination) {
                        (TOP_LEVEL_DESTINATIONS.keys + startDestination).toImmutableSet()
                    }
                )

                val navigator = koinInject<Navigator> { parametersOf(navigationState) }

                ObserveAsEvents(mainViewModel.events) { event ->
                    when (event) {
                        is MainEvent.OnSessionExpired -> {
                            navigator.clearBackstackAndNavigate(Route.Auth.SignIn)
                        }
                    }
                }

                NavigationRoot(
                    navigator = navigator,
                    navigationState = navigationState
                )
            }
        }
    }
}