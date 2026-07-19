package com.bksd.reflection.domain.analysis

import com.bksd.reflection.domain.model.DistressLevel
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.support.SupportConfig

private const val GENTLE_FALLBACK = "Thanks for taking a moment to write this down."

fun ReflectionResponse.toMomentReflection(): MomentReflection = when (analysis.distress) {
    DistressLevel.NONE, DistressLevel.MILD -> MomentReflection.Reflection(
        analysis = analysis,
        message = feedback ?: GENTLE_FALLBACK,
        question = question,
        coverImageUrl = coverImageUrl
    )

    DistressLevel.ELEVATED -> MomentReflection.Support(
        analysis = analysis,
        message = SupportConfig.ELEVATED_MESSAGE,
        mentalHealthLines = SupportConfig.mentalHealthLines,
        coverImageUrl = coverImageUrl
    )

    DistressLevel.CRISIS -> MomentReflection.Crisis(
        analysis = analysis,
        message = SupportConfig.CRISIS_MESSAGE,
        emergency = SupportConfig.emergency,
        crisisLines = SupportConfig.crisisLines,
        coverImageUrl = coverImageUrl
    )
}
