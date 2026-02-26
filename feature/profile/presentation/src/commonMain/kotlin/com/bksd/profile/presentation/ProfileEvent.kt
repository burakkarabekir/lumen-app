package com.bksd.profile.presentation

/**
 * One-shot side-effects for the Profile screen.
 */
sealed interface ProfileEvent {
    data object SignOutSuccess : ProfileEvent
    data object NavigateToPaywall : ProfileEvent
}
