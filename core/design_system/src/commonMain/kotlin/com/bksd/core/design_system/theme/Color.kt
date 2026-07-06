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

val InsightsLabelLight = Color(0xFFA09C95)
val InsightsTextLight = Color(0xFF1C1B1A)
val InsightsSubLight = Color(0xFF94908A)
val InsightsHairlineLight = Color(0x0F000000)
val InsightsDotGrayLight = Color(0xFFBCB8B2)

val InsightsLabelDark = Color(0xFF6E6C77)
val InsightsTextDark = Color(0xFFF2F1F4)
val InsightsSubDark = Color(0xFF7C7A85)
val InsightsHairlineDark = Color(0x14FFFFFF)
val InsightsDotGrayDark = Color(0xFF46454D)

val NewEntryTextLight = Color(0xFF1C1B1A)
val NewEntryBodyTextLight = Color(0xFF3A3733)
val NewEntrySubLight = Color(0xFF8A867F)
val NewEntryHairlineLight = Color(0x12000000)
val NewEntryPinBgLight = Color(0x247682D6)
val NewEntryPinFgLight = Color(0xFF5B6AD0)

val NewEntryTextDark = Color(0xFFF2F1F4)
val NewEntryBodyTextDark = Color(0xFFD9D7DE)
val NewEntrySubDark = Color(0xFF8B8993)
val NewEntryHairlineDark = Color(0x14FFFFFF)
val NewEntryPinBgDark = Color(0x2E7682D6)
val NewEntryPinFgDark = Color(0xFFA6AEEC)

val NewEntrySaveBg = Color(0xFF4F46E5)

val NavCapsuleLight = Color(0xFFFFFFFF)
val NavBorderLight = Color(0x0F000000)
val NavActiveFillLight = Color(0xFF4A47B5)
val NavActiveTextLight = Color(0xFFF4F3FB)
val NavIdleLight = Color(0xFF8A867F)
val NavPlusLight = Color(0xFF4A47B5)

val NavCapsuleDark = Color(0xFF191920)
val NavBorderDark = Color(0x12FFFFFF)
val NavActiveFillDark = Color(0xFF403C8E)
val NavActiveTextDark = Color(0xFFEDECF7)
val NavIdleDark = Color(0xFF9C9AA6)
val NavPlusDark = Color(0xFF9D99E8)

// ==================== AI Reflection Card ====================
val ReflectionCardIndigo = Color(0xFF4F46E5)
val ReflectionCardIconStart = Color(0xFF7682D6)
val ReflectionCardIconEnd = Color(0xFF5B6AD0)
val ReflectionCardTitleLight = Color(0xFF22203A)
val ReflectionCardMetaLight = Color(0xFF8A867F)
val ReflectionCardBodyLight = Color(0xFF3A3645)
val ReflectionCardChipBaseLight = Color(0xFF282446)
val ReflectionCardDisclaimerLight = Color(0xFFA09C95)
val ReflectionCardPillLight = Color(0xFF2FA876)
val ReflectionCardPillContentLight = Color(0xFF2A815E)
val ReflectionCardPillDark = Color(0xFF5EEAD4)
val ReflectionCardPillContentDark = Color(0xFF8FE9DA)
val ReflectionCardGradientTopLight = Color(0xFFEEF0FC)
val ReflectionCardGradientBottomLight = Color(0xFFE9E6F7)
val ReflectionCardGradientTopDark = Color(0xFF29314F)
val ReflectionCardGradientBottomDark = Color(0xFF1D2440)

// ==================== Support Card ====================
val SupportCardIconStart = Color(0xFF57B98F)
val SupportCardIconEnd = Color(0xFF3E9B77)
val SupportCardAccentLight = Color(0xFF2E7D57)
val SupportCardTitleLight = Color(0xFF16321F)
val SupportCardBodyLight = Color(0xFF39463F)
val SupportCardAccentDark = Color(0xFF8FD8B4)
val SupportCardGradientTopLight = Color(0xFFEEF7F1)
val SupportCardGradientBottomLight = Color(0xFFE0EFE7)
val SupportCardGradientTopDark = Color(0xFF1D3126)
val SupportCardGradientBottomDark = Color(0xFF142018)

// ==================== Crisis Card ====================
val CrisisCardIconStart = Color(0xFFE58060)
val CrisisCardIconEnd = Color(0xFFCD5A48)
val CrisisCardAccentLight = Color(0xFFB5473A)
val CrisisCardTitleLight = Color(0xFF4A1E1A)
val CrisisCardBodyLight = Color(0xFF43302B)
val CrisisCardAccentDark = Color(0xFFF0A08F)
val CrisisCardGradientTopLight = Color(0xFFFDF0EB)
val CrisisCardGradientBottomLight = Color(0xFFF7E4DB)
val CrisisCardGradientTopDark = Color(0xFF331F1A)
val CrisisCardGradientBottomDark = Color(0xFF241511)

// ==================== Mood Hues (icon / accent dot) ====================
val MoodHueHappy = Color(0xFFE0A21A)
val MoodHueGrateful = Color(0xFFC77FA8)
val MoodHueCalm = Color(0xFF2FA876)
val MoodHueLoved = Color(0xFFD9534A)
val MoodHueHopeful = Color(0xFF3F9C8D)
val MoodHueProud = Color(0xFFF2932B)
val MoodHueInspired = Color(0xFF8A6FBF)
val MoodHueReflective = Color(0xFF6E7AD0)
val MoodHueFocused = Color(0xFF5E9FD6)
val MoodHueNostalgic = Color(0xFFB0815B)
val MoodHueTired = Color(0xFF6E6C9E)
val MoodHueAnxious = Color(0xFFC08A1E)
val MoodHueStressed = Color(0xFFCF6F64)
val MoodHueFrustrated = Color(0xFFC5453B)
val MoodHueSad = Color(0xFF5E72C8)
val MoodHueMelancholic = Color(0xFF9CA3AF)
val MoodHueBored = Color(0xFF8E949B)

// ==================== Mood Chip (foreground) ====================
val MoodChipProud = Color(0xFF10B981)
val MoodChipHopeful = Color(0xFF34D399)
val MoodChipReflective = Color(0xFF6366F1)
val MoodChipInspired = Color(0xFFA855F7)
val MoodChipFocused = Color(0xFF2DD4BF)
val MoodChipLoved = Color(0xFFEC4899)
val MoodChipNostalgic = Color(0xFFD97706)
val MoodChipStressed = Color(0xFFCF6F64)
val MoodChipFrustrated = Color(0xFFC5453B)
val MoodChipSad = Color(0xFF6E7AD0)
val MoodChipMelancholic = Color(0xFF9CA3AF)
val MoodChipBored = Color(0xFF8E949B)

// ==================== Attachment Badges ====================
val AttachmentBadgePhoto = Color(0xFF2FA876)
val AttachmentBadgeVoice = Color(0xFFC99114)
val AttachmentBadgeVideo = Color(0xFF6E7AD0)
val AttachmentBadgeLink = Color(0xFF8A6FBF)
val AttachmentBadgeRemove = Color(0xFFE5484D)

// ==================== Media Type Icons ====================
val MediaTypePhoto = Color(0xFF2563EB)
val MediaTypeVideo = Color(0xFF7C3AED)
val MediaTypeAudio = Color(0xFF9333EA)
val MediaTypeLink = Color(0xFF0EA5E9)
val MediaTypeText = Color(0xFF475569)

// ==================== Attachment Chips ====================
val AttachmentChipVideoTileStart = Color(0xFF3A3F63)
val AttachmentChipVideoTileMid = Color(0xFF7682D6)
val AttachmentChipVideoTileEnd = Color(0xFFCF8676)
val AttachmentChipLinkTileStart = Color(0xFF8FE0CF)
val AttachmentChipLinkTileEnd = Color(0xFF34D399)
val AttachmentChipPlayIcon = Color(0xFF1C1B1A)
val AttachmentChipLinkAccent = Color(0xFFA78BFA)

// ==================== Streak Accents ====================
val StreakCoralCount = Color(0xFFE0524A)
val StreakCoralDot = Color(0xFFDC4B40)
val StreakCoralTrackStart = Color(0xFFEBA199)
val StreakVioletCount = Color(0xFF6E63D6)
val StreakVioletTrackStart = Color(0xFFA9A2E0)

// ==================== Insights Cards ====================
val InsightsStreakGradientStart = Color(0xFF30344F)
val InsightsStreakGradientEnd = Color(0xFF191B29)
val InsightsPlacesGradientStart = Color(0xFF2B2D48)
val InsightsPlacesGradientEnd = Color(0xFF1B1C2B)
val InsightsWrittenGradientStart = Color(0xFFD3796A)
val InsightsWrittenGradientEnd = Color(0xFFC0584F)
val InsightsJournaledGradientStart = Color(0xFFCF524B)
val InsightsJournaledGradientEnd = Color(0xFFA8474F)
val InsightsEntriesGradientStart = Color(0xFF7682D6)
val InsightsEntriesGradientEnd = Color(0xFF9281C6)
val InsightsEmptyStreakGradientStart = Color(0xFF2B2E46)
val InsightsEmptyStreakGradientEnd = Color(0xFF181A28)
val InsightsEmptyStreakTitle = Color(0xFFECECF2)
val InsightsEmptyStreakButton = Color(0xFF4F46E5)
val InsightsPlaceChipCount = Color(0xFFE9A98E)

// ==================== Profile Accents ====================
val ProfileAccentIndigo = Color(0xFF6E7AD0)
val ProfileAccentRed = Color(0xFFD0584F)
val ProfileAccentGreen = Color(0xFF2FA876)
val ProfileAccentViolet = Color(0xFF8A6FBF)
val ProfileAccentAmber = Color(0xFFE0A33A)
val ProfileHeroGradientStart = Color(0xFF7682D6)
val ProfileHeroGradientEnd = Color(0xFF9281C6)

// ==================== Cover / Hero / AI / Toolbar ====================
val CoverGradientStart = Color(0xFF7682D6)
val CoverGradientMid = Color(0xFF9281C6)
val CoverGradientEnd = Color(0xFFCF6F64)
val PaywallHeroGradientStart = Color(0xFF1A1C2E)
val PaywallHeroGradientEnd = Color(0xFF12141F)
val AiIconGradientStart = Color(0xFF7682D6)
val AiIconGradientEnd = Color(0xFFCF6F64)
val PaywallBestValueBadgeStart = Color(0xFFF5CD83)
val PaywallBestValueBadgeEnd = Color(0xFFE0A21A)
val PaywallBestValueBadgeText = Color(0xFF3A2A06)
val EntryToolbarPhoto = Color(0xFF2FA876)
val EntryToolbarVideo = Color(0xFF6E7AD0)
val EntryToolbarNote = Color(0xFFCF6F64)
val EntryToolbarVoice = Color(0xFFC99114)
val EntryToolbarLink = Color(0xFF8A6FBF)
val FavoriteRed = Color(0xFFE5484D)

// ==================== Share Card Styles ====================
val ShareAuroraTop = Color(0xFF8E97DB)
val ShareAuroraBottom = Color(0xFFD79BA0)
val ShareAuroraLogoBg = Color(0x33FFFFFF)
val ShareAuroraChipBg = Color(0x33FFFFFF)
val SharePaperBg = Color(0xFFF3EEE4)
val SharePaperText = Color(0xFF2A2620)
val SharePaperSub = Color(0xFF8A8175)
val SharePaperChipBg = Color(0x14000000)
val ShareInkBgTop = Color(0xFF1E2338)
val ShareInkBgBottom = Color(0xFF12162A)
val ShareInkText = Color(0xFFF2F1F4)
val ShareInkSub = Color(0xFF9A9DB0)
val ShareInkChipBg = Color(0x1FFFFFFF)
val SharePhotoTop = Color(0xFF8091E6)
val SharePhotoBottom = Color(0xFFB2BCF2)

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