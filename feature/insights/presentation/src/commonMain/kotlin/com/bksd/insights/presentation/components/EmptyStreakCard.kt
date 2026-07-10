package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.insightsEmptyStreakGradient
import com.bksd.core.design_system.theme.insightsEmptyStreakTitle
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.journal_every_day_to_build
import com.bksd.insights.presentation.no_current_streak
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EmptyStreakCard() {
    val gradient = MaterialTheme.colorScheme.extended.insightsEmptyStreakGradient
    val titleColor = MaterialTheme.colorScheme.extended.insightsEmptyStreakTitle
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(StreakCardHeight)
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(Brush.linearGradient(gradient))
    ) {
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(MaterialTheme.dimens.spacing.lg)) {
            MenuDot(tint = Color.White.copy(alpha = 0.7f), bg = Color.White.copy(alpha = 0.10f))
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(MaterialTheme.dimens.spacing.xxl),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.no_current_streak),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            Text(
                text = stringResource(Res.string.journal_every_day_to_build),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.42f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun EmptyStreakCardPreview() {
    AppTheme {
        Box(Modifier.padding(MaterialTheme.dimens.spacing.xl)) {
            EmptyStreakCard()
        }
    }
}
