package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.rememberInsightsPalette

@Composable
internal fun StreakDetailCard(detail: com.bksd.insights.presentation.StreakDetail, palette: InsightsPalette) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(StreakCardHeight)
            .clip(RoundedCornerShape(22.dp))
            .background(palette.surface)
            .padding(18.dp)
    ) {
        Text(
            text = detail.title,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = palette.text
        )
        Spacer(Modifier.height(16.dp))
        StreakLineRow("Daily Streak", detail.daily, palette)
        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = palette.hair)
        Spacer(Modifier.height(14.dp))
        StreakLineRow("Weekly Streak", detail.weekly, palette)
    }
}

@Preview
@Composable
private fun StreakDetailCardPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.pageBg).padding(18.dp)) {
            StreakDetailCard(SampleLongest, palette)
        }
    }
}
