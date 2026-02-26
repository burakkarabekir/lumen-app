package com.bksd.core.design_system.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Emotion colors
    val emotionJoy: Color,
    val emotionSadness: Color,
    val emotionAnger: Color,
    val emotionFear: Color,
    val emotionSurprise: Color,
    val emotionCalm: Color,
    val emotionGratitude: Color,

    // Emotion backgrounds
    val emotionJoyBg: Color,
    val emotionSadnessBg: Color,
    val emotionAngerBg: Color,
    val emotionFearBg: Color,
    val emotionSurpriseBg: Color,
    val emotionCalmBg: Color,
    val emotionGratitudeBg: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentIndigo: Color,
    val accentGrey: Color,
)

val LightExtendedColors = ExtendedColors(
    primaryHover = LumenBrand700,
    destructiveHover = LumenRed600,
    destructiveSecondaryOutline = LumenRed200,
    disabledOutline = LumenBase200,
    disabledFill = LumenBase150,
    successOutline = LumenGreen100,
    success = LumenGreen500,
    onSuccess = LumenBase0,
    secondaryFill = LumenBase100,

    textPrimary = LumenBase1000,
    textTertiary = LumenBase700,
    textSecondary = LumenBase800,
    textPlaceholder = LumenBase600,
    textDisabled = LumenBase400,

    surfaceLower = LumenBase50,
    surfaceHigher = LumenBase100,
    surfaceOutline = LumenBase1000Alpha14,
    overlay = LumenBase1000Alpha80,

    emotionJoy = EmotionJoy,
    emotionSadness = EmotionSadness,
    emotionAnger = EmotionAnger,
    emotionFear = EmotionFear,
    emotionSurprise = EmotionSurprise,
    emotionCalm = EmotionCalm,
    emotionGratitude = EmotionGratitude,

    emotionJoyBg = EmotionJoyBgLight,
    emotionSadnessBg = EmotionSadnessBgLight,
    emotionAngerBg = EmotionAngerBgLight,
    emotionFearBg = EmotionFearBgLight,
    emotionSurpriseBg = EmotionSurpriseBgLight,
    emotionCalmBg = EmotionCalmBgLight,
    emotionGratitudeBg = EmotionGratitudeBgLight,

    accentBlue = LumenAccentBlue,
    accentPurple = LumenAccentPurple,
    accentViolet = LumenAccentViolet,
    accentPink = LumenAccentPink,
    accentOrange = LumenAccentOrange,
    accentYellow = LumenAccentYellow,
    accentGreen = LumenAccentGreen,
    accentTeal = LumenAccentTeal,
    accentIndigo = LumenAccentIndigo,
    accentGrey = LumenAccentGrey,
)

val DarkExtendedColors = ExtendedColors(
    primaryHover = LumenBrand500,
    destructiveHover = LumenRed600,
    destructiveSecondaryOutline = LumenRed200,
    disabledOutline = LumenBase800,
    disabledFill = LumenBase900,
    successOutline = LumenBrand500Alpha40,
    success = LumenBrand500,
    onSuccess = LumenBase1000,
    secondaryFill = LumenBase900,

    textPrimary = LumenBase0,
    textTertiary = LumenBase300,
    textSecondary = LumenBase200,
    textPlaceholder = LumenBase500,
    textDisabled = LumenBase600,

    surfaceLower = LumenBase1000,
    surfaceHigher = LumenBase900,
    surfaceOutline = LumenBase100Alpha10Alt,
    overlay = LumenBase1000Alpha80,

    emotionJoy = EmotionJoy,
    emotionSadness = EmotionSadness,
    emotionAnger = EmotionAnger,
    emotionFear = EmotionFear,
    emotionSurprise = EmotionSurprise,
    emotionCalm = EmotionCalm,
    emotionGratitude = EmotionGratitude,

    emotionJoyBg = EmotionJoyBgDark,
    emotionSadnessBg = EmotionSadnessBgDark,
    emotionAngerBg = EmotionAngerBgDark,
    emotionFearBg = EmotionFearBgDark,
    emotionSurpriseBg = EmotionSurpriseBgDark,
    emotionCalmBg = EmotionCalmBgDark,
    emotionGratitudeBg = EmotionGratitudeBgDark,

    accentBlue = LumenAccentBlue,
    accentPurple = LumenAccentPurple,
    accentViolet = LumenAccentViolet,
    accentPink = LumenAccentPink,
    accentOrange = LumenAccentOrange,
    accentYellow = LumenAccentYellow,
    accentGreen = LumenAccentGreen,
    accentTeal = LumenAccentTeal,
    accentIndigo = LumenAccentIndigo,
    accentGrey = LumenAccentGrey,
)

val LightColorScheme = lightColorScheme(
    primary = LumenBrand600,
    onPrimary = LumenBase0,
    primaryContainer = LumenBrand100,
    onPrimaryContainer = LumenBrand900,

    secondary = LumenBase700,
    onSecondary = LumenBase0,
    secondaryContainer = LumenBase100,
    onSecondaryContainer = LumenBase900,

    tertiary = LumenBrand800,
    onTertiary = LumenBase0,
    tertiaryContainer = LumenBrand100,
    onTertiaryContainer = LumenBrand1000,

    error = LumenRed500,
    onError = LumenBase0,
    errorContainer = LumenRed100,
    onErrorContainer = LumenRed600,

    background = LumenBase0,
    onBackground = LumenBase1000,
    surface = LumenBase0,
    onSurface = LumenBase1000,
    surfaceVariant = LumenBase100,
    onSurfaceVariant = LumenBase800,

    outline = LumenBase1000Alpha8,
    outlineVariant = LumenBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = LumenBrand500,
    onPrimary = LumenBase0,
    primaryContainer = LumenBrand900,
    onPrimaryContainer = LumenBrand300,

    secondary = LumenBase400,
    onSecondary = LumenBase1000,
    secondaryContainer = LumenBase900,
    onSecondaryContainer = LumenBase200,

    tertiary = LumenBrand400,
    onTertiary = LumenBase1000,
    tertiaryContainer = LumenBrand900,
    onTertiaryContainer = LumenBrand300,

    error = LumenRed400,
    onError = LumenBase0,
    errorContainer = LumenRed600,
    onErrorContainer = LumenRed200,

    background = LumenBase1000,
    onBackground = LumenBase0,
    surface = LumenBase950,
    onSurface = LumenBase0,
    surfaceVariant = LumenBase900,
    onSurfaceVariant = LumenBase200,

    outline = LumenBase100Alpha10,
    outlineVariant = LumenBase800,
)