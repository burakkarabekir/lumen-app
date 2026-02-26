package com.bksd.onboarding.presentation

/**
 * Sealed interface defining all user actions in the Onboarding feature.
 */
sealed interface OnboardingAction {
    data object Next : OnboardingAction
    data object Previous : OnboardingAction
    data object Skip : OnboardingAction
    data object Complete : OnboardingAction
}
