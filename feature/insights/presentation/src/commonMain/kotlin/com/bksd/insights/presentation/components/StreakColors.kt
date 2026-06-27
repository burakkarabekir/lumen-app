package com.bksd.insights.presentation.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.insights.presentation.StreakAccent

internal data class StreakColors(val count: Color, val dot: Color, val track: Brush)

internal fun streakColors(accent: StreakAccent, palette: InsightsPalette): StreakColors =
    when (accent) {
        StreakAccent.CORAL -> StreakColors(
            count = Color(0xFFE0524A),
            dot = Color(0xFFDC4B40),
            track = Brush.horizontalGradient(listOf(Color(0xFFEBA199), Color(0xFFDC4B40)))
        )

        StreakAccent.VIOLET -> StreakColors(
            count = Color(0xFF6E63D6),
            dot = Color(0xFF6E63D6),
            track = Brush.horizontalGradient(listOf(Color(0xFFA9A2E0), Color(0xFF6E63D6)))
        )

        StreakAccent.NEUTRAL -> StreakColors(
            count = palette.text,
            dot = palette.dotGray,
            track = SolidColor(palette.dotGray)
        )
    }
