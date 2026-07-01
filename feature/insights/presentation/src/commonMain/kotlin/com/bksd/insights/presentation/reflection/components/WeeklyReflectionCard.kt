package com.bksd.insights.presentation.reflection.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.insights.presentation.reflection.reflectionHexColor
import com.bksd.reflection.domain.model.ReflectionTheme
import com.bksd.reflection.domain.model.WeeklyReflection

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeeklyReflectionCard(
    reflection: WeeklyReflection,
    onViewFull: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val colors = weeklyCardColors(dark)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF7682D6), Color(0xFF5B6AD0))))
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Weekly Reflection",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    color = colors.title
                )
                Text(
                    text = "Based on ${reflection.entryCount} ${entryWord(reflection.entryCount)} · ${reflection.rangeLabel}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.meta,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
            if (reflection.summary.isNotBlank()) {
                Spacer(Modifier.width(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(11.dp))
                        .background(colors.pillBg)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(colors.pillContent)
                    )
                    Text(
                        text = reflection.summary,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.pillContent
                    )
                }
            }
        }

        Text(
            text = reflection.narrative,
            fontSize = 14.5.sp,
            lineHeight = 23.5.sp,
            fontWeight = FontWeight.Medium,
            color = colors.body,
            modifier = Modifier.padding(top = 15.dp)
        )

        if (reflection.themes.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier.padding(top = 14.dp)
            ) {
                reflection.themes.forEach { theme ->
                    ReflectionThemeChip(
                        label = theme.label,
                        color = reflectionHexColor(theme.colorHex),
                        chipColor = colors.chipBg,
                        textColor = colors.chipText
                    )
                }
            }
        }

        reflection.questions.firstOrNull()?.takeIf { it.isNotBlank() }?.let { question ->
            Column(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.promptBg)
                    .border(1.dp, colors.promptBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 13.dp)
            ) {
                Text(
                    text = "A QUESTION TO SIT WITH",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.7.sp,
                    color = colors.promptLabel
                )
                Text(
                    text = question,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.promptText,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.hairline)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 14.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onViewFull)
            ) {
                Text(
                    text = "View full reflection",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.title
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = colors.title,
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = "Private to you",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.meta
            )
        }
    }
}

private fun entryWord(count: Int): String = if (count == 1) "entry" else "entries"

private data class WeeklyCardColors(
    val surface: Color,
    val border: Color,
    val title: Color,
    val meta: Color,
    val body: Color,
    val pillBg: Color,
    val pillContent: Color,
    val chipBg: Color,
    val chipText: Color,
    val promptBg: Color,
    val promptBorder: Color,
    val promptLabel: Color,
    val promptText: Color,
    val hairline: Color,
)

private fun weeklyCardColors(dark: Boolean): WeeklyCardColors = if (dark) {
    WeeklyCardColors(
        surface = Color(0xFF232A45),
        border = Color.White.copy(alpha = 0.07f),
        title = Color.White,
        meta = Color.White.copy(alpha = 0.5f),
        body = Color.White.copy(alpha = 0.87f),
        pillBg = Color(0xFF5EEAD4).copy(alpha = 0.16f),
        pillContent = Color(0xFF8FE9DA),
        chipBg = Color.White.copy(alpha = 0.10f),
        chipText = Color.White.copy(alpha = 0.9f),
        promptBg = Color.White.copy(alpha = 0.06f),
        promptBorder = Color.White.copy(alpha = 0.09f),
        promptLabel = Color.White.copy(alpha = 0.5f),
        promptText = Color.White,
        hairline = Color.White.copy(alpha = 0.10f),
    )
} else {
    WeeklyCardColors(
        surface = Color(0xFFECEEFB),
        border = Color(0xFF4F46E5).copy(alpha = 0.14f),
        title = Color(0xFF22203A),
        meta = Color(0xFF8A867F),
        body = Color(0xFF3A3645),
        pillBg = Color(0xFF2FA876).copy(alpha = 0.14f),
        pillContent = Color(0xFF2A815E),
        chipBg = Color(0xFF282446).copy(alpha = 0.06f),
        chipText = Color(0xFF3A3645),
        promptBg = Color(0xFF4F46E5).copy(alpha = 0.07f),
        promptBorder = Color(0xFF4F46E5).copy(alpha = 0.14f),
        promptLabel = Color(0xFF8A867F),
        promptText = Color(0xFF22203A),
        hairline = Color.Black.copy(alpha = 0.07f),
    )
}

private fun sampleWeekly() = WeeklyReflection(
    narrative = "This week leaned quiet. Your mornings kept showing up — fog, coffee, the balcony — " +
        "with a steady thread of gratitude underneath. You wrote most on the days you let yourself slow down.",
    summary = "Calm week",
    themes = listOf(
        ReflectionTheme("Calm", "#2FA876", 5),
        ReflectionTheme("Gratitude", "#C77FA8", 3),
        ReflectionTheme("Mornings", "#E0A21A", 3),
        ReflectionTheme("Rest", "#6E7AD0", 2),
    ),
    questions = listOf("What would it take to protect one slow morning each week?"),
    entryCount = 6,
    rangeLabel = "Jun 21–27",
    generatedAtMs = 0L
)

@Preview
@Composable
private fun WeeklyReflectionCardLightPreview() {
    AppTheme {
        WeeklyReflectionCard(reflection = sampleWeekly(), onViewFull = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun WeeklyReflectionCardDarkPreview() {
    AppTheme(darkTheme = true) {
        WeeklyReflectionCard(reflection = sampleWeekly(), onViewFull = {}, modifier = Modifier.padding(16.dp))
    }
}
