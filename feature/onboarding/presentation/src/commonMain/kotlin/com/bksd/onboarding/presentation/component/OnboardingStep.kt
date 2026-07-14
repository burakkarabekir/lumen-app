package com.bksd.onboarding.presentation.component

import androidx.compose.runtime.Immutable
import com.bksd.onboarding.presentation.Res
import com.bksd.onboarding.presentation.btn_get_started
import com.bksd.onboarding.presentation.btn_next
import com.bksd.onboarding.presentation.btn_start_journaling
import com.bksd.onboarding.presentation.onboarding_body_1
import com.bksd.onboarding.presentation.onboarding_body_2
import com.bksd.onboarding.presentation.onboarding_body_3
import com.bksd.onboarding.presentation.onboarding_title_1
import com.bksd.onboarding.presentation.onboarding_title_2
import com.bksd.onboarding.presentation.onboarding_title_3
import org.jetbrains.compose.resources.StringResource

@Immutable
data class OnboardingStep(
    val titleRes: StringResource,
    val bodyRes: StringResource,
    val ctaRes: StringResource,
    val showArrow: Boolean,
    val advances: Boolean,
)

val onboardingSteps: List<OnboardingStep> = listOf(
    OnboardingStep(
        titleRes = Res.string.onboarding_title_1,
        bodyRes = Res.string.onboarding_body_1,
        ctaRes = Res.string.btn_get_started,
        showArrow = true,
        advances = true,
    ),
    OnboardingStep(
        titleRes = Res.string.onboarding_title_2,
        bodyRes = Res.string.onboarding_body_2,
        ctaRes = Res.string.btn_next,
        showArrow = true,
        advances = true,
    ),
    OnboardingStep(
        titleRes = Res.string.onboarding_title_3,
        bodyRes = Res.string.onboarding_body_3,
        ctaRes = Res.string.btn_start_journaling,
        showArrow = false,
        advances = false,
    ),
)
