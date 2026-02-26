package com.bksd.onboarding.presentation

/**
 * One-time events emitted by the Onboarding ViewModel.
 */
sealed interface OnboardingEvent {
    data object NavigateToAuth : OnboardingEvent
}
