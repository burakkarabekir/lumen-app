package com.bksd.core.design_system.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Immutable
data class InsightsPalette(
    val pageBg: Color,
    val label: Color,
    val surface: Color,
    val text: Color,
    val sub: Color,
    val hair: Color,
    val dotGray: Color,
)

@Composable
fun rememberInsightsPalette(): InsightsPalette {
    val extended = MaterialTheme.colorScheme.extended
    return remember(extended) {
        InsightsPalette(
            pageBg = extended.insightsPageBg,
            label = extended.insightsLabel,
            surface = extended.insightsSurface,
            text = extended.insightsText,
            sub = extended.insightsSub,
            hair = extended.insightsHairline,
            dotGray = extended.insightsDotGray,
        )
    }
}
