package com.bksd.lumen.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bksd.auth.presentation.resetpassword.ResetPasswordRoot
import com.bksd.auth.presentation.signin.SignInRoot
import com.bksd.auth.presentation.signup.SignUpRoot
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.insights.presentation.InsightsRoot
import com.bksd.journal.presentation.detail.MomentDetailRoot
import com.bksd.journal.presentation.journal.JournalRoot
import com.bksd.lumen.navigation.route.Route
import com.bksd.lumen.navigation.route.Route.Companion.shouldShowBottomBar
import com.bksd.moment.presentation.create.CreateMomentRoot
import com.bksd.onboarding.presentation.OnboardingRoot
import com.bksd.paywall.presentation.PaywallRoot
import com.bksd.profile.presentation.ProfileRoot
import kotlinx.collections.immutable.toImmutableSet
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Main.Journal,
        topLevelRoutes = TOP_LEVEL_DESTINATIONS.keys.toImmutableSet()
    )

    val navigator = koinInject<Navigator> { parametersOf(navigationState) }

    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }

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
                    // ==================== Onboarding ====================
                    entry<Route.Onboarding> {
                        OnboardingRoot(
                            onComplete = { navigator.navigateToSignIn() }
                        )
                    }

                    // ==================== Auth ====================
                    entry<Route.Auth.SignIn> {
                        SignInRoot(
                            onNavigateToHome = { navigator.navigateToJournal() },
                            onNavigateToSignUp = { navigator.navigateToSignUp() },
                            onNavigateToForgotPassword = { navigator.navigateToResetPassword() }
                        )
                    }
                    entry<Route.Auth.SignUp> {
                        SignUpRoot(
                            onNavigateToHome = { navigator.navigateToJournal() },
                            onNavigateToSignIn = { navigator.navigateToSignIn() }
                        )
                    }
                    entry<Route.Auth.ResetPassword> {
                        ResetPasswordRoot(
                            onNavigateToSignIn = { navigator.navigateToSignIn() }
                        )
                    }

                    // ==================== Main Tabs ====================
                    entry<Route.Main.Journal> {
                        JournalRoot(
                            onNavigateToDetail = { momentId ->
                                navigator.navigateToMomentDetail(
                                    momentId
                                )
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

                    // ==================== Detail Screens ====================
                    entry<Route.MomentDetail> { backStackEntry ->
                        MomentDetailRoot(
                            momentId = backStackEntry.momentId,
                            onNavigateBack = navigator::goBack,
                            onNavigateToEdit = { /* Will implement edit navigation when edit screen exists */ }
                        )
                    }
                    entry<Route.CreateMoment> {
                        CreateMomentRoot(
                            onNavigateBack = { navigator.goBack() },
                            onShowSnackbar = { println("CreateMoment Snackbar: $it") }
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

/**
 * Temporary placeholder screen used during scaffolding.
 * Will be replaced with real feature screens as they're implemented.
 */
@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}