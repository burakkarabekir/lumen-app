package com.bksd.lumen.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bksd.auth.presentation.resetpassword.ResetPasswordRoot
import com.bksd.auth.presentation.signin.SignInRoot
import com.bksd.auth.presentation.signup.SignUpRoot
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.insights.presentation.InsightsRoot
import com.bksd.journal.presentation.detail.MomentDetailRoot
import com.bksd.journal.presentation.journal.JournalRoot
import com.bksd.lumen.main.MainEvent
import com.bksd.lumen.main.MainViewModel
import com.bksd.lumen.navigation.route.Route
import com.bksd.lumen.navigation.route.Route.Companion.shouldShowBottomBar
import com.bksd.moment.presentation.create.CreateMomentRoot
import com.bksd.onboarding.presentation.OnboardingRoot
import com.bksd.paywall.presentation.PaywallRoot
import com.bksd.profile.presentation.ProfileRoot
import kotlinx.collections.immutable.toImmutableSet
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val mainViewModel = koinViewModel<MainViewModel>()
    val mainState by mainViewModel.state.collectAsState()

    if (!mainState.isReady) return

    val navigationState = rememberNavigationState(
        startRoute = Route.Main.Journal,
        topLevelRoutes = remember { TOP_LEVEL_DESTINATIONS.keys.toImmutableSet() }
    )

    val navigator = koinInject<Navigator> { parametersOf(navigationState) }
    val snackbarHostState = remember { SnackbarHostState() }

    var navigationReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!mainState.isLoggedIn) {
            navigator.clearBackstackAndNavigate(Route.Auth.SignIn)
        }
        navigationReady = true
    }

    ObserveAsEvents(mainViewModel.events) { event ->
        when (event) {
            is MainEvent.OnSessionExpired -> {
                navigator.clearBackstackAndNavigate(Route.Auth.SignIn)
            }
        }
    }

    if (!navigationReady) return

    AppScaffold(
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = navigationState.currentRoute.shouldShowBottomBar(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AppNavigationBar(
                    selectedKey = navigationState.topLevelRoute,
                    onSelectKey = {
                        navigator.navigate(it)
                    }
                )
            }
        }
    ) {
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            onBack = navigator::goBack,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            popTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Onboarding> {
                        OnboardingRoot(
                            onComplete = { navigator.navigateToSignIn() }
                        )
                    }

                    entry<Route.Auth.SignIn> {
                        SignInRoot(
                            onNavigateToHome = {
                                navigator.clearBackstackAndNavigate(Route.Main.Journal)
                            },
                            onNavigateToSignUp = { navigator.navigateToSignUp() },
                            onNavigateToForgotPassword = { navigator.navigateToResetPassword() }
                        )
                    }
                    entry<Route.Auth.SignUp> {
                        SignUpRoot(
                            onNavigateToHome = {
                                navigator.clearBackstackAndNavigate(Route.Main.Journal)
                            },
                            onNavigateToSignIn = { navigator.navigateToSignIn() }
                        )
                    }
                    entry<Route.Auth.ResetPassword> {
                        ResetPasswordRoot(
                            onNavigateToSignIn = { navigator.navigateToSignIn() }
                        )
                    }

                    entry<Route.Main.Journal> {
                        JournalRoot(
                            onNavigateToDetail = { momentId ->
                                navigator.navigateToMomentDetail(momentId)
                            },
                            onNavigateToCreate = { navigator.navigateToCreateMoment() }
                        )
                    }
                    entry<Route.Main.Insights> {
                        InsightsRoot()
                    }
                    entry<Route.Main.Profile> {
                        ProfileRoot(
                            onNavigateToSignIn = { navigator.navigateToSignIn() },
                            onNavigateToPaywall = { navigator.navigateToPaywall() }
                        )
                    }

                    entry<Route.MomentDetail> { backStackEntry ->
                        MomentDetailRoot(
                            momentId = backStackEntry.momentId,
                            onNavigateBack = navigator::goBack,
                            onNavigateToEdit = {}
                        )
                    }
                    entry<Route.CreateMoment> {
                        CreateMomentRoot(
                            onNavigateBack = { navigator.goBack() },
                        )
                    }
                    entry<Route.Paywall> {
                        PaywallRoot(
                            onDismiss = { navigator.goBack() }
                        )
                    }
                }
            )
        )
    }
}