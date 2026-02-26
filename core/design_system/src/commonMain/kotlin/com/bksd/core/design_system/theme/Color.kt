package com.bksd.core.design_system.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==================== Lumen Brand Colors ====================
val LumenBrand1000 = Color(0xFF040D3A)
val LumenBrand900 = Color(0xFF091B6B)
val LumenBrand800 = Color(0xFF0D2A9C)
val LumenBrand700 = Color(0xFF103AC6)
val LumenBrand600 = Color(0xFF1349EC) // Primary from Stitch
val LumenBrand500 = Color(0xFF3D6AF0)
val LumenBrand400 = Color(0xFF678BF4)
val LumenBrand300 = Color(0xFF91ACF7)
val LumenBrand200 = Color(0xFFBBCDFA)
val LumenBrand100 = Color(0xFFE5EEFE)

val LumenBrand500Alpha40 = Color(0x663D6AF0)
val LumenBrand600Alpha20 = Color(0x331349EC)
val LumenBrand600Alpha10 = Color(0x1A1349EC)

// ==================== Base / Neutral Colors ====================
val LumenBase1000 = Color(0xFF0A0E1A)
val LumenBase1000Alpha8 = Color(0x140A0E1A)
val LumenBase1000Alpha80 = Color(0xCC0A0E1A)
val LumenBase950 = Color(0xFF111629)
val LumenBase900 = Color(0xFF1C2237)
val LumenBase800 = Color(0xFF2E3551)
val LumenBase700 = Color(0xFF4A5270)
val LumenBase600 = Color(0xFF636B8A)
val LumenBase500 = Color(0xFF7E86A3)
val LumenBase400 = Color(0xFF9BA2BA)
val LumenBase300 = Color(0xFFB8BDD1)
val LumenBase200 = Color(0xFFD4D8E5)
val LumenBase150 = Color(0xFFE2E5EE)
val LumenBase100 = Color(0xFFF2F3F7)
val LumenBase50 = Color(0xFFF8F9FB)
val LumenBase0 = Color(0xFFFFFFFF)

val LumenBase100Alpha10 = Color(0x1AF2F3F7)
val LumenBase1000Alpha14 = Color(0x240A0E1A)
val LumenBase100Alpha10Alt = Color(0x1AF2F3F7)

// ==================== Semantic Colors ====================
val LumenRed600 = Color(0xFFAA142A)
val LumenRed500 = Color(0xFFDA233E)
val LumenRed400 = Color(0xFFE85A6F)
val LumenRed200 = Color(0xFFFF7987)
val LumenRed100 = Color(0xFFFFE5E8)

val LumenGreen600 = Color(0xFF0F8A4F)
val LumenGreen500 = Color(0xFF17B26A)
val LumenGreen100 = Color(0xFFE6F9EF)

val LumenAmber500 = Color(0xFFF79009)
val LumenAmber100 = Color(0xFFFFF4E5)

// ==================== Emotion Colors ====================
val EmotionJoy = Color(0xFFFFC857)
val EmotionSadness = Color(0xFF5B8DEF)
val EmotionAnger = Color(0xFFEF5B5B)
val EmotionFear = Color(0xFF8B5CF6)
val EmotionSurprise = Color(0xFF34D399)
val EmotionDisgust = Color(0xFF9CA3AF)
val EmotionCalm = Color(0xFF67E8F9)
val EmotionGratitude = Color(0xFFF472B6)
val EmotionAnxiety = Color(0xFFD97706)
val EmotionHope = Color(0xFF10B981)

// Emotion background tints (15% alpha for dark mode, solid pastel for light mode)
val EmotionJoyBgLight = Color(0xFFFFF8E1)
val EmotionSadnessBgLight = Color(0xFFE8F0FE)
val EmotionAngerBgLight = Color(0xFFFDE8E8)
val EmotionFearBgLight = Color(0xFFF0E8FE)
val EmotionSurpriseBgLight = Color(0xFFE6FAF3)
val EmotionCalmBgLight = Color(0xFFE6FAFE)
val EmotionGratitudeBgLight = Color(0xFFFDE8F3)

val EmotionJoyBgDark = Color(0x26FFC857)
val EmotionSadnessBgDark = Color(0x265B8DEF)
val EmotionAngerBgDark = Color(0x26EF5B5B)
val EmotionFearBgDark = Color(0x268B5CF6)
val EmotionSurpriseBgDark = Color(0x2634D399)
val EmotionCalmBgDark = Color(0x2667E8F9)
val EmotionGratitudeBgDark = Color(0x26F472B6)

// ==================== Accent Colors (15% alpha) ====================
val LumenAccentBlue = Color(0x26678BF4)
val LumenAccentPurple = Color(0x268B5CF6)
val LumenAccentViolet = Color(0x26A855F7)
val LumenAccentPink = Color(0x26F472B6)
val LumenAccentOrange = Color(0x26FB923C)
val LumenAccentYellow = Color(0x26FBBF24)
val LumenAccentGreen = Color(0x2634D399)
val LumenAccentTeal = Color(0x262DD4BF)
val LumenAccentIndigo = Color(0x266366F1)
val LumenAccentGrey = Color(0x269BA2BA)

// ==================== Text Colors ====================
val TextFieldBgPassive = Color(0xFFF2F3F7)
val TextPrimary = Color(0xFF0A0E1A)
val TextSecondary = Color(0xFF1C2237)
val TextTertiary = Color(0xFF4A5270)
val TextPlaceholder = Color(0xFF636B8A)
val TextDisabled = Color(0xFF9BA2BA)
val DisabledOutline = Color(0xFFD4D8E5)
val DestructiveSecondaryOutline = Color(0xFFFF7987)

// ==================== Gradient Helpers ====================

val ColorScheme.bgGradient: Brush
    get() = Brush.verticalGradient(
        listOf(
            LumenBrand200.copy(alpha = 0.3f),
            LumenBrand100.copy(alpha = 0.2f),
        )
    )

val ColorScheme.buttonGradient: Brush
    get() = Brush.verticalGradient(
        listOf(
            LumenBrand500,
            LumenBrand600,
        )
    )

val ColorScheme.buttonGradientPressed: Brush
    get() = Brush.verticalGradient(
        listOf(
            LumenBrand500,
            LumenBrand700,
        )
    )

val ColorScheme.bgCardDaily: Brush
    get() = Brush.verticalGradient(
        listOf(
            LumenBrand600.copy(alpha = 0.6f),
            LumenBrand1000.copy(alpha = 0.1f),
        )
    )

val ColorScheme.primary90: Color
    get() = LumenBrand100

val ColorScheme.primary95: Color
    get() = LumenBrand100.copy(alpha = 0.5f)

val ColorScheme.secondary95: Color
    get() = LumenBase100

val ColorScheme.secondary70: Color
    get() = LumenBase400

val ColorScheme.textFieldBgPassive: Color
    get() = TextFieldBgPassive

val ColorScheme.textPrimary: Color
    get() = TextPrimary

val ColorScheme.textSecondary: Color
    get() = TextSecondary

val ColorScheme.textTertiary: Color
    get() = TextTertiary

val ColorScheme.textPlaceholder: Color
    get() = TextPlaceholder

val ColorScheme.textDisabled: Color
    get() = TextDisabled

val ColorScheme.disabledFill: Color
    get() = TextPrimary

val ColorScheme.disabledOutline: Color
    get() = DisabledOutline

val ColorScheme.destructiveSecondaryOutline: Color
    get() = DestructiveSecondaryOutline