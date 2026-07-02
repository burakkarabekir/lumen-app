package com.bksd.insights.presentation.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.bksd.core.design_system.theme.ExtendedColors
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.streakCoralCount
import com.bksd.core.design_system.theme.streakCoralDot
import com.bksd.core.design_system.theme.streakCoralTrackStart
import com.bksd.core.design_system.theme.streakVioletCount
import com.bksd.core.design_system.theme.streakVioletTrackStart
import com.bksd.insights.presentation.StreakAccent

internal data class StreakColors(val count: Color, val dot: Color, val track: Brush)

internal fun streakColors(accent: StreakAccent, palette: InsightsPalette, extended: ExtendedColors): StreakColors =
    when (accent) {
        StreakAccent.CORAL -> StreakColors(
            count = extended.streakCoralCount,
            dot = extended.streakCoralDot,
            track = Brush.horizontalGradient(listOf(extended.streakCoralTrackStart, extended.streakCoralDot))
        )

        StreakAccent.VIOLET -> StreakColors(
            count = extended.streakVioletCount,
            dot = extended.streakVioletCount,
            track = Brush.horizontalGradient(listOf(extended.streakVioletTrackStart, extended.streakVioletCount))
        )

        StreakAccent.NEUTRAL -> StreakColors(
            count = palette.text,
            dot = palette.dotGray,
            track = SolidColor(palette.dotGray)
        )
    }
