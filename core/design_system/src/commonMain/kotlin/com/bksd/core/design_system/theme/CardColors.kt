package com.bksd.core.design_system.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ReflectionCardColors(
    val surfaceGradient: List<Color>,
    val border: Color,
    val iconStart: Color,
    val iconEnd: Color,
    val title: Color,
    val meta: Color,
    val body: Color,
    val chipBg: Color,
    val chipText: Color,
    val promptBg: Color,
    val promptBorder: Color,
    val promptLabel: Color,
    val promptText: Color,
    val hairline: Color,
    val disclaimer: Color,
    val pillBg: Color,
    val pillContent: Color,
    val loadingSurfaceGradient: List<Color>,
    val loadingBorder: Color,
)

@Immutable
data class DistressCardColors(
    val iconStart: Color,
    val iconEnd: Color,
    val accent: Color,
    val surfaceGradient: List<Color>,
    val border: Color,
    val title: Color,
    val body: Color,
    val rowBg: Color,
)

val ReflectionChipDots: List<Color> = listOf(
    MoodHueCalm,
    MoodHueHappy,
    MoodHueReflective,
    MoodHueGrateful,
    MoodHueStressed,
)

val LightReflectionCardColors = ReflectionCardColors(
    surfaceGradient = listOf(ReflectionCardGradientTopLight, ReflectionCardGradientBottomLight),
    border = ReflectionCardIndigo.copy(alpha = 0.14f),
    iconStart = ReflectionCardIconStart,
    iconEnd = ReflectionCardIconEnd,
    title = ReflectionCardTitleLight,
    meta = ReflectionCardMetaLight,
    body = ReflectionCardBodyLight,
    chipBg = ReflectionCardChipBaseLight.copy(alpha = 0.06f),
    chipText = ReflectionCardBodyLight,
    promptBg = ReflectionCardIndigo.copy(alpha = 0.07f),
    promptBorder = ReflectionCardIndigo.copy(alpha = 0.14f),
    promptLabel = ReflectionCardMetaLight,
    promptText = ReflectionCardTitleLight,
    hairline = Color.Black.copy(alpha = 0.07f),
    disclaimer = ReflectionCardDisclaimerLight,
    pillBg = ReflectionCardPillLight.copy(alpha = 0.14f),
    pillContent = ReflectionCardPillContentLight,
    loadingSurfaceGradient = listOf(
        ReflectionCardIndigo.copy(alpha = 0.06f),
        ReflectionCardIndigo.copy(alpha = 0.02f),
    ),
    loadingBorder = ReflectionCardIndigo.copy(alpha = 0.13f),
)

val DarkReflectionCardColors = ReflectionCardColors(
    surfaceGradient = listOf(ReflectionCardGradientTopDark, ReflectionCardGradientBottomDark),
    border = Color.White.copy(alpha = 0.07f),
    iconStart = ReflectionCardIconStart,
    iconEnd = ReflectionCardIconEnd,
    title = Color.White,
    meta = Color.White.copy(alpha = 0.5f),
    body = Color.White.copy(alpha = 0.87f),
    chipBg = Color.White.copy(alpha = 0.10f),
    chipText = Color.White.copy(alpha = 0.9f),
    promptBg = Color.White.copy(alpha = 0.06f),
    promptBorder = Color.White.copy(alpha = 0.09f),
    promptLabel = Color.White.copy(alpha = 0.5f),
    promptText = Color.White,
    hairline = Color.White.copy(alpha = 0.10f),
    disclaimer = Color.White.copy(alpha = 0.45f),
    pillBg = ReflectionCardPillDark.copy(alpha = 0.16f),
    pillContent = ReflectionCardPillContentDark,
    loadingSurfaceGradient = listOf(
        Color.White.copy(alpha = 0.06f),
        Color.White.copy(alpha = 0.02f),
    ),
    loadingBorder = Color.White.copy(alpha = 0.08f),
)

val LightSupportCardColors = DistressCardColors(
    iconStart = SupportCardIconStart,
    iconEnd = SupportCardIconEnd,
    accent = SupportCardAccentLight,
    surfaceGradient = listOf(SupportCardGradientTopLight, SupportCardGradientBottomLight),
    border = SupportCardIconEnd.copy(alpha = 0.24f),
    title = SupportCardTitleLight,
    body = SupportCardBodyLight,
    rowBg = Color.White,
)

val DarkSupportCardColors = DistressCardColors(
    iconStart = SupportCardIconStart,
    iconEnd = SupportCardIconEnd,
    accent = SupportCardAccentDark,
    surfaceGradient = listOf(SupportCardGradientTopDark, SupportCardGradientBottomDark),
    border = Color.White.copy(alpha = 0.07f),
    title = Color.White,
    body = Color.White.copy(alpha = 0.87f),
    rowBg = Color.White.copy(alpha = 0.06f),
)

val LightCrisisCardColors = DistressCardColors(
    iconStart = CrisisCardIconStart,
    iconEnd = CrisisCardIconEnd,
    accent = CrisisCardAccentLight,
    surfaceGradient = listOf(CrisisCardGradientTopLight, CrisisCardGradientBottomLight),
    border = CrisisCardIconEnd.copy(alpha = 0.24f),
    title = CrisisCardTitleLight,
    body = CrisisCardBodyLight,
    rowBg = Color.White,
)

val DarkCrisisCardColors = DistressCardColors(
    iconStart = CrisisCardIconStart,
    iconEnd = CrisisCardIconEnd,
    accent = CrisisCardAccentDark,
    surfaceGradient = listOf(CrisisCardGradientTopDark, CrisisCardGradientBottomDark),
    border = Color.White.copy(alpha = 0.07f),
    title = Color.White,
    body = Color.White.copy(alpha = 0.88f),
    rowBg = Color.White.copy(alpha = 0.06f),
)
