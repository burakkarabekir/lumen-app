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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bksd.auth.presentation.resetpassword.ResetPasswordRoot
import com.bksd.auth.presentation.signin.SignInRoot
import com.bksd.auth.presentation.signup.SignUpRoot
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.domain.connectivity.NetworkMonitor
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.insights.presentation.InsightsRoot
import com.bksd.insights.presentation.places.PlacesRoot
import com.bksd.insights.presentation.reflection.full.WeeklyReflectionDetailRoot
import com.bksd.journal.presentation.detail.MomentDetailRoot
import com.bksd.journal.presentation.journal.JournalRoot
import com.bksd.lumen.connectivity.ConnectivityBanner
import com.bksd.lumen.consent.ConsentGate
import com.bksd.lumen.lock.LockGate
import com.bksd.lumen.main.MainEvent
import com.bksd.lumen.main.MainViewModel
import com.bksd.lumen.navigation.route.Route
import com.bksd.lumen.navigation.route.Route.Companion.shouldShowBottomBar
import com.bksd.lumen.reminder.ReminderLaunchSignal
import com.bksd.lumen.welcome.LoginWelcomeSignal
import com.bksd.lumen.welcome.WelcomeGate
import com.bksd.lumen.welcome.WelcomeGreeting
import com.bksd.moment.presentation.create.CreateMomentRoot
import com.bksd.onboarding.presentation.OnboardingRoot
import com.bksd.paywall.presentation.PaywallRoot
import com.bksd.profile.presentation.AboutRoot
import com.bksd.profile.presentation.CloudSyncRoot
import com.bksd.profile.presentation.EditProfileRoot
import com.bksd.profile.presentation.ExportJournalRoot
import com.bksd.profile.presentation.HelpRoot
import com.bksd.profile.presentation.LegalDocumentRoot
import com.bksd.profile.presentation.LegalRoot
import com.bksd.profile.presentation.LockPrivacyRoot
import com.bksd.profile.presentation.ManagePremiumRoot
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
    val welcomeSignal = koinInject<LoginWelcomeSignal>()
    val reminderLaunchSignal = koinInject<ReminderLaunchSignal>()
    val snackbarHostState = remember { SnackbarHostState() }

    val networkMonitor = koinInject<NetworkMonitor>()
    val isOnline by networkMonitor.isOnline.collectAsState(initial = true)

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

    LaunchedEffect(navigationReady) {
        if (!navigationReady) return@LaunchedEffect
        reminderLaunchSignal.pending.collect { open ->
            if (open) {
                if (mainViewModel.state.value.isLoggedIn) {
                    navigator.navigateToCreateMoment()
                }
                reminderLaunchSignal.clear()
            }
        }
    }

    if (!navigationReady) return

    Box(modifier = modifier.fillMaxSize()) {
        AppScaffold(
            snackbarHostState = snackbarHostState,
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
                    },
                    onAddClick = { navigator.navigateToCreateMoment() }
                )
            }
        }
    ) {
        NavDisplay(
            modifier = Modifier
                .fillMaxSize(),
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
                                welcomeSignal.request(WelcomeGreeting.RETURNING)
                                navigator.clearBackstackAndNavigate(Route.Main.Journal)
                            },
                            onNavigateToSignUp = { navigator.navigateToSignUp() },
                            onNavigateToForgotPassword = { navigator.navigateToResetPassword() }
                        )
                    }
                    entry<Route.Auth.SignUp> {
                        SignUpRoot(
                            onNavigateToHome = {
                                welcomeSignal.request(WelcomeGreeting.NEW)
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
                            onNavigateToDetail = { momentId, isEditing ->
                                navigator.navigateToMomentDetail(momentId, isEditing)
                            },
                            onNavigateToProfile = { navigator.navigateToProfile() }
                        )
                    }
                    entry<Route.Main.Insights> {
                        InsightsRoot(
                            onViewFullReflection = { navigator.navigateToWeeklyReflection() },
                            onNavigateToPaywall = { navigator.navigateToPaywall() },
                            onNavigateToPlaces = { navigator.navigateToPlaces() }
                        )
                    }
                    entry<Route.Profile> {
                        ProfileRoot(
                            onBack = { navigator.goBack() },
                            onNavigateToSignIn = { navigator.navigateToSignIn() },
                            onNavigateToPaywall = { navigator.navigateToPaywall() },
                            onNavigateToManagePremium = { navigator.navigateToManagePremium() },
                            onNavigateToCloudSync = { navigator.navigateToCloudSync() },
                            onNavigateToLockPrivacy = { navigator.navigateToLockPrivacy() },
                            onNavigateToExportJournal = { navigator.navigateToExportJournal() },
                            onNavigateToLegal = { navigator.navigateToLegal() },
                            onNavigateToEditProfile = { navigator.navigateToEditProfile() },
                            onNavigateToAbout = { navigator.navigateToAbout() },
                            onNavigateToHelp = { navigator.navigateToHelp() }
                        )
                    }

                    entry<Route.EditProfile> {
                        EditProfileRoot(
                            onBack = { navigator.goBack() }
                        )
                    }

                    entry<Route.About> {
                        AboutRoot(
                            onBack = { navigator.goBack() }
                        )
                    }

                    entry<Route.Help> {
                        HelpRoot(
                            onBack = { navigator.goBack() }
                        )
                    }

                    entry<Route.MomentDetail> { backStackEntry ->
                        MomentDetailRoot(
                            momentId = backStackEntry.momentId,
                            isEditing = backStackEntry.isEditing,
                            onNavigateBack = navigator::goBack,
                            onNavigateToPaywall = { navigator.navigateToPaywall() }
                        )
                    }
                    entry<Route.WeeklyReflection> {
                        WeeklyReflectionDetailRoot(
                            onBack = navigator::goBack,
                            onOpenMoment = { navigator.navigateToMomentDetail(it) }
                        )
                    }
                    entry<Route.Places> {
                        PlacesRoot(onBack = navigator::goBack)
                    }
                    entry<Route.CreateMoment> {
                        CreateMomentRoot(
                            onNavigateBack = { navigator.goBack() },
                            onNavigateToPaywall = { navigator.navigateToPaywall() }
                        )
                    }
                    entry<Route.Paywall> {
                        PaywallRoot(
                            onDismiss = { navigator.goBack() }
                        )
                    }
                    entry<Route.ManagePremium> {
                        ManagePremiumRoot(
                            onBack = { navigator.goBack() },
                            onNavigateToPaywall = { navigator.navigateToPaywall() }
                        )
                    }
                    entry<Route.CloudSync> {
                        CloudSyncRoot(
                            onBack = { navigator.goBack() }
                        )
                    }
                    entry<Route.LockPrivacy> {
                        LockPrivacyRoot(
                            onBack = { navigator.goBack() },
                            onOpenDocument = { url, title ->
                                navigator.navigateToLegalDocument(url, title)
                            }
                        )
                    }
                    entry<Route.ExportJournal> {
                        ExportJournalRoot(
                            onBack = { navigator.goBack() }
                        )
                    }
                    entry<Route.Legal> {
                        LegalRoot(
                            onBack = { navigator.goBack() },
                            onOpenDocument = { url, title ->
                                navigator.navigateToLegalDocument(url, title)
                            }
                        )
                    }
                    entry<Route.LegalDocument> { backStackEntry ->
                        LegalDocumentRoot(
                            url = backStackEntry.url,
                            title = backStackEntry.title,
                            onBack = { navigator.goBack() }
                        )
                    }
                }
            )
        )
        }

        ConnectivityBanner(
            isOnline = isOnline,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = MaterialTheme.dimens.size.topBar)
        )

        WelcomeGate()

        ConsentGate()

        LockGate()
    }
}