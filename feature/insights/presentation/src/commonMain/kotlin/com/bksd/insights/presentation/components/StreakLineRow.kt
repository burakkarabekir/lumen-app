package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.rememberInsightsPalette
import com.bksd.insights.presentation.StreakAccent
import com.bksd.insights.presentation.StreakLine

@Composable
internal fun StreakLineRow(label: String, line: StreakLine, palette: InsightsPalette) {
    val colors = streakColors(line.accent, palette)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = palette.text
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${line.count}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.count
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = line.unit,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.count,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        DotProgress(count = line.count, colors = colors, ringColor = palette.surface)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(line.startDate, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = palette.sub)
            Text(line.endDate, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = palette.sub)
        }
    }
}

@Preview
@Composable
private fun StreakLineRowPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.surface).padding(18.dp)) {
            StreakLineRow(
                label = "Daily Streak",
                line = StreakLine(7, "Days", "May 24, 2024", "May 31, 2024", StreakAccent.CORAL),
                palette = palette
            )
        }
    }
}
