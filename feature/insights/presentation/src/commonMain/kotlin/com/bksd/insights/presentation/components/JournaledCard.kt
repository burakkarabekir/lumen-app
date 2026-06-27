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
import com.bksd.insights.presentation.JournaledStat

@Composable
internal fun JournaledCard(stat: JournaledStat, modifier: Modifier) {
    Column(
        modifier = modifier
            .height(StatCardHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFFCF524B), Color(0xFFA8474F))))
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Journaled",
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
                text = "Days",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            MiniStat("${stat.thisMonth}", "This Month")
            MiniStat("${stat.thisYear}", "This Year")
        }
    }
}

@Preview
@Composable
private fun JournaledCardPreview() {
    AppTheme {
        Box(Modifier.padding(18.dp).width(180.dp)) {
            JournaledCard(stat = SampleJournaled, modifier = Modifier)
        }
    }
}
