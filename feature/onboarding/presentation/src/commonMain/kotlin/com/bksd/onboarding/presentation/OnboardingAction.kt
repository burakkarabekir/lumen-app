package com.bksd.onboarding.presentation

sealed interface OnboardingAction {
    data object Complete : OnboardingAction
}
