package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.insightsJournaledGradient
import com.bksd.insights.presentation.JournaledStat
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.stat_days
import com.bksd.insights.presentation.stat_journaled
import com.bksd.insights.presentation.stat_this_month
import com.bksd.insights.presentation.stat_this_year
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun JournaledCard(stat: JournaledStat, modifier: Modifier) {
    val gradient = MaterialTheme.colorScheme.extended.insightsJournaledGradient
    Column(
        modifier = modifier
            .height(StatCardHeight)
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(Brush.verticalGradient(gradient))
            .padding(vertical = MaterialTheme.dimens.spacing.lg, horizontal = MaterialTheme.dimens.spacing.md),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.stat_journaled),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${stat.days}",
                fontSize = 42.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = stringResource(Res.string.stat_days),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            MiniStat("${stat.thisMonth}", stringResource(Res.string.stat_this_month))
            MiniStat("${stat.thisYear}", stringResource(Res.string.stat_this_year))
        }
    }
}

@Preview
@Composable
private fun JournaledCardPreview() {
    AppTheme {
        Box(Modifier.padding(MaterialTheme.dimens.spacing.xl).width(180.dp)) {
            JournaledCard(stat = SampleJournaled, modifier = Modifier)
        }
    }
}
