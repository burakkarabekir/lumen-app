package com.bksd.insights.presentation

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

private val StreakCardHeight = 250.dp

@Composable
internal fun StreaksCarousel(state: InsightsState, palette: InsightsPalette) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    Column {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> CurrentStreakCard(state.currentStreak)
                1 -> StreakDetailCard(state.longest, palette)
                else -> StreakDetailCard(state.recent, palette)
            }
        }
        Spacer(Modifier.height(14.dp))
        PagerDots(count = 3, current = pagerState.currentPage, palette = palette)
    }
}

@Composable
internal fun CurrentStreakCard(streak: CurrentStreak) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(StreakCardHeight)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF3A3A63), Color(0xFF2A2D45), Color(0xFF191B29))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.radialGradient(listOf(Color(0x40554DA8), Color(0x00000000))))
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Streak",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.86f)
                )
                MenuDot(tint = Color.White.copy(alpha = 0.7f), bg = Color.White.copy(alpha = 0.10f))
            }
            Column {
                Text(
                    text = "${streak.value}",
                    fontSize = 70.sp,
                    lineHeight = 70.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-2.5).sp,
                    color = Color.White
                )
                Text(
                    text = streak.unit,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.5f))) {
                            append("You've journaled at least once a week since ")
                        }
                        withStyle(
                            SpanStyle(
                                color = Color.White.copy(alpha = 0.82f),
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(streak.since)
                        }
                        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.5f))) {
                            append(".")
                        }
                    },
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
internal fun StreakDetailCard(detail: StreakDetail, palette: InsightsPalette) {
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

@Composable
private fun StreakLineRow(label: String, line: StreakLine, palette: InsightsPalette) {
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

@Composable
private fun DotProgress(count: Int, colors: StreakColors, ringColor: Color) {
    val dots = min(count, 12)
    Box(
        modifier = Modifier.fillMaxWidth().height(12.dp),
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
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(ringColor)
                        .padding(1.5.dp)
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

@Composable
internal fun EmptyStreakCard(onSetSchedule: () -> Unit) {
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
                    .clickable(onClick = onSetSchedule)
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
                    text = "Set Schedule",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun PagerDots(count: Int, current: Int, palette: InsightsPalette) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { i ->
            val active = i == current
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .height(6.dp)
                    .width(if (active) 18.dp else 6.dp)
                    .clip(CircleShape)
                    .background(if (active) palette.sub else palette.dotGray)
            )
        }
    }
}

@Composable
private fun MenuDot(tint: Color, bg: Color) {
    Box(
        modifier = Modifier.size(26.dp).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.MoreHoriz,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}

private data class StreakColors(val count: Color, val dot: Color, val track: Brush)

private fun streakColors(accent: StreakAccent, palette: InsightsPalette): StreakColors =
    when (accent) {
        StreakAccent.CORAL -> StreakColors(
            count = Color(0xFFE0524A),
            dot = Color(0xFFDC4B40),
            track = Brush.horizontalGradient(listOf(Color(0xFFEBA199), Color(0xFFDC4B40)))
        )
        StreakAccent.VIOLET -> StreakColors(
            count = Color(0xFF6E63D6),
            dot = Color(0xFF6E63D6),
            track = Brush.horizontalGradient(listOf(Color(0xFFA9A2E0), Color(0xFF6E63D6)))
        )
        StreakAccent.NEUTRAL -> StreakColors(
            count = palette.text,
            dot = palette.dotGray,
            track = SolidColor(palette.dotGray)
        )
    }
