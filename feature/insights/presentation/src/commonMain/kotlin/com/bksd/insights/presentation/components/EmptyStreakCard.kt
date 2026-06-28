package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme

@Composable
internal fun EmptyStreakCard(onSetReminder: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(StreakCardHeight)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF2B2E46), Color(0xFF181A28))))
    ) {
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            MenuDot(tint = Color.White.copy(alpha = 0.7f), bg = Color.White.copy(alpha = 0.10f))
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No Current Streak",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFECECF2)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Journal every day to build a streak.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.42f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF4F46E5))
                    .clickable(onClick = onSetReminder)
                    .padding(horizontal = 17.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = "Set Reminder",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmptyStreakCardPreview() {
    AppTheme {
        Box(Modifier.padding(18.dp)) {
            EmptyStreakCard(onSetReminder = {})
        }
    }
}
