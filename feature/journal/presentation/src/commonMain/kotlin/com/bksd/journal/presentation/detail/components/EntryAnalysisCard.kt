package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.model.MoodValence

private val ChipDotColors = listOf(
    Color(0xFF2FA876),
    Color(0xFFE0A21A),
    Color(0xFF6E7AD0),
    Color(0xFFC77FA8),
    Color(0xFFCF6F64),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EntryAnalysisCard(
    reflection: MomentReflection.Reflection,
    modifier: Modifier = Modifier,
    generatedLabel: String = "Generated on save",
) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val colors = reflectionCardColors(dark)

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
                    .background(Brush.linearGradient(listOf(colors.iconStart, colors.iconEnd)))
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
                    text = "AI Analysis",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    color = colors.title
                )
                Text(
                    text = generatedLabel,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.meta,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }

        Text(
            text = reflection.message,
            fontSize = 14.5.sp,
            lineHeight = 23.5.sp,
            fontWeight = FontWeight.Medium,
            color = colors.body,
            modifier = Modifier.padding(top = 15.dp)
        )

        if (reflection.analysis.themes.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier.padding(top = 14.dp)
            ) {
                reflection.analysis.themes.forEachIndexed { index, theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(13.dp))
                            .background(colors.chipBg)
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(ChipDotColors[index % ChipDotColors.size])
                        )
                        Text(
                            text = theme,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.chipText
                        )
                    }
                }
            }
        }

        val question = reflection.question
        if (!question.isNullOrBlank()) {
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
                .padding(top = 15.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.hairline)
        )
        Text(
            text = "Lumen AI can make mistakes. Reflections are private to you.",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.disclaimer,
            modifier = Modifier.padding(top = 13.dp)
        )
    }
}

private data class ReflectionCardColors(
    val surface: Color,
    val border: Color,
    val iconStart: Color,
    val iconEnd: Color,
    val title: Color,
    val meta: Color,
    val body: Color,
    val chipBg: Color,
    val chipText: Color,
    val promptBg: Color,
    val promptBorder: Color,
    val promptLabel: Color,
    val promptText: Color,
    val hairline: Color,
    val disclaimer: Color,
)

private fun reflectionCardColors(dark: Boolean): ReflectionCardColors = if (dark) {
    ReflectionCardColors(
        surface = Color(0xFF232A45),
        border = Color.White.copy(alpha = 0.07f),
        iconStart = Color(0xFF7682D6),
        iconEnd = Color(0xFF5B6AD0),
        title = Color.White,
        meta = Color.White.copy(alpha = 0.5f),
        body = Color.White.copy(alpha = 0.87f),
        chipBg = Color.White.copy(alpha = 0.10f),
        chipText = Color.White.copy(alpha = 0.9f),
        promptBg = Color.White.copy(alpha = 0.06f),
        promptBorder = Color.White.copy(alpha = 0.09f),
        promptLabel = Color.White.copy(alpha = 0.5f),
        promptText = Color.White,
        hairline = Color.White.copy(alpha = 0.10f),
        disclaimer = Color.White.copy(alpha = 0.45f),
    )
} else {
    ReflectionCardColors(
        surface = Color(0xFFECEEFB),
        border = Color(0xFF4F46E5).copy(alpha = 0.14f),
        iconStart = Color(0xFF7682D6),
        iconEnd = Color(0xFF5B6AD0),
        title = Color(0xFF22203A),
        meta = Color(0xFF8A867F),
        body = Color(0xFF3A3645),
        chipBg = Color(0xFF282446).copy(alpha = 0.06f),
        chipText = Color(0xFF3A3645),
        promptBg = Color(0xFF4F46E5).copy(alpha = 0.07f),
        promptBorder = Color(0xFF4F46E5).copy(alpha = 0.14f),
        promptLabel = Color(0xFF8A867F),
        promptText = Color(0xFF22203A),
        hairline = Color.Black.copy(alpha = 0.07f),
        disclaimer = Color(0xFFA09C95),
    )
}

private fun sampleReflection() = MomentReflection.Reflection(
    analysis = EntryAnalysis(
        summary = "Calm, grateful morning.",
        moodValence = MoodValence.POSITIVE,
        moodConfidence = 0.82,
        dominantEmotions = listOf("calm", "gratitude"),
        themes = listOf("Calm", "Mornings", "Stillness"),
        distress = com.bksd.reflection.domain.model.DistressLevel.NONE,
        distressRationale = ""
    ),
    message = "There's a gentle steadiness running through this entry. You're savoring slowness — " +
        "the fog, the quiet, the coffee — and naming what you want more of. The tone reads calm and quietly grateful.",
    question = "What would it take to protect one slow morning each week?"
)

@Preview
@Composable
private fun EntryAnalysisCardLightPreview() {
    AppTheme {
        EntryAnalysisCard(
            reflection = sampleReflection(),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun EntryAnalysisCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryAnalysisCard(
            reflection = sampleReflection(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
