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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.insights.presentation.CurrentStreak

@Composable
internal fun CurrentStreakCard(weekly: CurrentStreak, daily: CurrentStreak) {
    var showDaily by rememberSaveable { mutableStateOf(false) }
    val streak = if (showDaily) daily else weekly
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(StreakCardHeight)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF30344F), Color(0xFF191B29))))
    ) {
        StreakBlobs()
        Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
            Text(
                text = "Current Streak",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.86f)
            )
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${streak.value}",
                    fontSize = 70.sp,
                    lineHeight = 58.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-2.5).sp,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = streak.unit,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = buildAnnotatedString {
                        val dim = SpanStyle(color = Color.White.copy(alpha = 0.5f))
                        if (streak.since.isNotBlank()) {
                            withStyle(dim) {
                                append(
                                    if (showDaily) "You've journaled every day since "
                                    else "You've journaled at least once a week since "
                                )
                            }
                            withStyle(
                                SpanStyle(
                                    color = Color.White.copy(alpha = 0.82f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append(streak.since)
                            }
                            withStyle(dim) { append(".") }
                        } else {
                            withStyle(dim) { append("Journal today to start a daily streak.") }
                        }
                    },
                    fontSize = 12.5.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        MenuDot(
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 16.dp, end = 18.dp),
            tint = Color.White.copy(alpha = 0.6f),
            bg = Color.White.copy(alpha = 0.10f),
            icon = Icons.Default.SwapHoriz,
            onClick = { showDaily = !showDaily }
        )
    }
}

@Preview
@Composable
private fun CurrentStreakCardPreview() {
    AppTheme {
        Box(Modifier.padding(18.dp)) {
            CurrentStreakCard(SampleCurrentStreak, SampleDailyStreak)
        }
    }
}
