package com.bksd.journal.presentation.journal.components

import androidx.compose.ui.graphics.Color
import com.bksd.core.design_system.theme.ExtendedColors
import com.bksd.core.domain.model.Mood

/**
 * Maps each [Mood] to a (background, text) color pair.
 *
 * Every mood has a unique color identity:
 * - HAPPY      → Warm gold/amber      ☀️
 * - CALM       → Soft cyan/sky        🌊
 * - INSPIRED   → Vibrant violet       💜
 * - REFLECTIVE → Deep indigo          🔮
 * - GRATEFUL   → Rose pink            🌸
 * - FOCUSED    → Steel teal           🎯
 * - TIRED      → Muted slate blue     😴
 * - ANXIOUS    → Warm amber/ochre     ⚠️
 * - LOVED      → Soft magenta/fuchsia 💕
 * - MELANCHOLIC→ Rain blue            🌧️
 * - PROUD      → Emerald green        🏆
 * - HOPEFUL    → Fresh mint           🌱
 * - NOSTALGIC  → Warm sepia/brown     📷
 */
internal fun moodColors(
    mood: Mood,
    extendedColors: ExtendedColors
): Pair<Color, Color> = when (mood) {
    // ── Positive / Warm ──
    Mood.HAPPY -> extendedColors.emotionJoyBg to extendedColors.emotionJoy             // Gold #FFC857
    Mood.PROUD -> Color(0x2610B981) to Color(0xFF10B981)                               // Emerald
    Mood.GRATEFUL -> extendedColors.emotionGratitudeBg to extendedColors.emotionGratitude // Pink #F472B6

    // ── Calm / Serene ──
    Mood.CALM -> extendedColors.emotionCalmBg to extendedColors.emotionCalm           // Cyan #67E8F9
    Mood.HOPEFUL -> Color(0x2634D399) to Color(0xFF34D399)                               // Mint
    Mood.REFLECTIVE -> Color(0x266366F1) to Color(0xFF6366F1)                               // Indigo

    // ── Creative / Stimulating ──
    Mood.INSPIRED -> Color(0x26A855F7) to Color(0xFFA855F7)                               // Violet
    Mood.FOCUSED -> Color(0x262DD4BF) to Color(0xFF2DD4BF)                               // Teal

    // ── Tender / Emotional ──
    Mood.LOVED -> Color(0x26EC4899) to Color(0xFFEC4899)                               // Fuchsia
    Mood.NOSTALGIC -> Color(0x26D97706) to Color(0xFFD97706)                               // Sepia/Amber

    // ── Low / Heavy ──
    Mood.TIRED -> extendedColors.emotionSadnessBg to extendedColors.emotionSadness     // Blue #5B8DEF
    Mood.ANXIOUS -> extendedColors.emotionAngerBg to extendedColors.emotionAnger         // Red #EF5B5B
    Mood.STRESSED -> Color(0x26CF6F64) to Color(0xFFCF6F64)
    Mood.FRUSTRATED -> Color(0x26C5453B) to Color(0xFFC5453B)
    Mood.SAD -> Color(0x266E7AD0) to Color(0xFF6E7AD0)
    Mood.MELANCHOLIC -> Color(0x269CA3AF) to Color(0xFF9CA3AF)                              // Slate grey
    Mood.BORED -> Color(0x268E949B) to Color(0xFF8E949B)
}
