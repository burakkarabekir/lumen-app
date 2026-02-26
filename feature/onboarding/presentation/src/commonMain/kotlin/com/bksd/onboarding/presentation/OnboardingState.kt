package com.bksd.onboarding.presentation

import androidx.compose.runtime.Immutable

/**
 * Immutable UI state for the Onboarding feature.
 * Represents the current step in the onboarding pager.
 */
@Immutable
data class OnboardingState(
    val currentStep: Int = 0,
    val totalSteps: Int = 4,
) {
    val isFirstStep: Boolean get() = currentStep == 0
    val isLastStep: Boolean get() = currentStep == totalSteps - 1
    val progress: Float get() = (currentStep + 1).toFloat() / totalSteps
}
