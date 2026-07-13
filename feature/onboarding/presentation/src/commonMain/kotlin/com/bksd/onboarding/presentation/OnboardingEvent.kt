package com.bksd.onboarding.presentation

sealed interface OnboardingEvent {
    data object Finished : OnboardingEvent
}
