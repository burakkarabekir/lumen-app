package com.bksd.onboarding.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bksd.core.design_system.theme.LumenBrand500

/**
 * Data describing a single onboarding step.
 * Immutable to prevent recomposition issues.
 */
@Immutable
data class OnboardingStep(
    val title: String,
    val highlightedWord: String,
    val subtitle: String,
    val accentColor: Color = LumenBrand500,
)

val onboardingSteps = listOf(
    OnboardingStep(
        title = "Capture Your",
        highlightedWord = "Moments",
        subtitle = "Record your thoughts using voice, text, photos, or video — effortlessly.",
    ),
    OnboardingStep(
        title = "Reflect with",
        highlightedWord = "Clarity",
        subtitle = "Turn your daily experiences into meaningful insights.",
    ),
    OnboardingStep(
        title = "Discover Your",
        highlightedWord = "Patterns",
        subtitle = "AI helps you understand emotional trends and life themes over time.",
    ),
    OnboardingStep(
        title = "Your Moments",
        highlightedWord = "Stay Yours",
        subtitle = "Private by design. Secure. Always under your control.",
    ),
)
