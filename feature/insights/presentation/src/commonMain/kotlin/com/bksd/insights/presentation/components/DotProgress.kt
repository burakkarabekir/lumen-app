package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberInsightsPalette
import com.bksd.insights.presentation.StreakAccent
import kotlin.math.min

@Composable
internal fun DotProgress(count: Int, colors: StreakColors, ringColor: Color) {
    val dots = min(count, 12)
    Box(
        modifier = Modifier.fillMaxWidth().height(MaterialTheme.dimens.icon.xs),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(CircleShape)
                .background(colors.track)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(dots) {
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.icon.xs)
                        .clip(CircleShape)
                        .background(ringColor)
                        .padding(MaterialTheme.dimens.spacing.xxs)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(colors.dot)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DotProgressPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.surface).padding(MaterialTheme.dimens.spacing.lg)) {
            DotProgress(
                count = 7,
                colors = streakColors(StreakAccent.CORAL, palette, MaterialTheme.colorScheme.extended),
                ringColor = palette.surface
            )
        }
    }
}
