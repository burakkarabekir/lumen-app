package com.bksd.journal.presentation.journal.components

import androidx.compose.ui.graphics.Color
import com.bksd.core.design_system.theme.ExtendedColors
import com.bksd.core.design_system.theme.moodChip
import com.bksd.core.domain.model.Mood

internal fun moodColors(
    mood: Mood,
    extendedColors: ExtendedColors
): Pair<Color, Color> = extendedColors.moodChip(mood)
