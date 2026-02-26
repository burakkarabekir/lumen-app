package com.bksd.lumen.navigation

import androidx.navigation3.runtime.NavKey
import com.bksd.core.domain.logging.AppLogger
import com.bksd.lumen.navigation.route.Route

/**
 * Navigator handles all navigation actions in the Lumen app.
 * Provides type-safe navigation methods and graceful error handling.
 */
class Navigator(
    val state: NavigationState,
    private val logger: AppLogger,
) {

    /**
     * Generic navigate function with type safety.
     * Validates that the route is a valid NavKey at compile time.
     */
    fun <T : NavKey> navigate(route: T) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
            logger.debug("Navigated to top-level route: $route")
        } else {
            val currentStack = state.backStacks[state.topLevelRoute]
            if (currentStack != null) {
                currentStack.add(route)
                logger.debug("Added route $route to stack of ${state.topLevelRoute}")
            } else {
                logger.debug("Failed to navigate: back stack for ${state.topLevelRoute} is null")
            }
        }
    }

    // ==================== Main Tab Navigation ====================

    fun navigateToJournal() = navigate(Route.Main.Journal)

    fun navigateToInsights() = navigate(Route.Main.Insights)

    fun navigateToProfile() = navigate(Route.Main.Profile)

    // ==================== Auth Navigation ====================

    fun navigateToSignIn() = navigate(Route.Auth.SignIn)

    fun navigateToSignUp() = navigate(Route.Auth.SignUp)

    fun navigateToResetPassword() = navigate(Route.Auth.ResetPassword)

    // ==================== Detail Navigation ====================

    fun navigateToMomentDetail(momentId: String) = navigate(Route.MomentDetail(momentId))

    fun navigateToCreateMoment() = navigate(Route.CreateMoment)

    fun navigateToPaywall() = navigate(Route.Paywall)

    fun navigateToOnboarding() = navigate(Route.Onboarding)

    // ==================== Back Navigation ====================

    /**
     * Handles back navigation with graceful error handling.
     * Falls back to start route if navigation stack is corrupted or empty.
     */
    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute] ?: run {
            logger.debug("Back stack for ${state.topLevelRoute} doesn't exist. Falling back to start route.")
            state.topLevelRoute = state.startRoute
            return
        }

        if (currentStack.isEmpty()) {
            logger.debug("Back stack is empty. Staying at current route: ${state.topLevelRoute}")
            return
        }

        val currentRoute = currentStack.last()

        if (currentRoute == state.topLevelRoute) {
            logger.debug("At top-level route. Navigating to start route: ${state.startRoute}")
            state.topLevelRoute = state.startRoute
        } else {
            val removed = currentStack.removeLastOrNull()
            logger.debug("Removed route from stack: $removed")
        }
    }

    /**
     * Navigate back to the start route.
     */
    fun navigateToStart() {
        logger.debug("Navigating to start route: ${state.startRoute}")
        state.topLevelRoute = state.startRoute
    }

    /**
     * Check if back navigation is possible.
     */
    fun canGoBack(): Boolean {
        val currentStack = state.backStacks[state.topLevelRoute] ?: return false
        val currentRoute = currentStack.lastOrNull() ?: return false
        return currentRoute != state.topLevelRoute
    }
}