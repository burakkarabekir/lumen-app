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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberInsightsPalette

@Composable
internal fun StreakDetailCard(detail: com.bksd.insights.presentation.StreakDetail, palette: InsightsPalette) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(StreakCardHeight)
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(palette.surface)
            .padding(MaterialTheme.dimens.spacing.xl)
    ) {
        Text(
            text = detail.title,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = palette.text
        )
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
        StreakLineRow("Daily Streak", detail.daily, palette)
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
        HorizontalDivider(color = palette.hair)
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
        StreakLineRow("Weekly Streak", detail.weekly, palette)
    }
}

@Preview
@Composable
private fun StreakDetailCardPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.xl)) {
            StreakDetailCard(SampleLongest, palette)
        }
    }
}
