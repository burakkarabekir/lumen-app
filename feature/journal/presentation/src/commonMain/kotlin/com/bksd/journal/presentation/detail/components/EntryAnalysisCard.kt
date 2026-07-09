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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.ReflectionChipDots
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.ai_analysis_title
import com.bksd.journal.presentation.ai_generated_on_save
import com.bksd.journal.presentation.ai_question_to_sit_with
import com.bksd.journal.presentation.ai_reflection_disclaimer
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.model.MoodValence
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EntryAnalysisCard(
    reflection: MomentReflection.Reflection,
    modifier: Modifier = Modifier,
    generatedLabel: String = stringResource(Res.string.ai_generated_on_save),
) {
    val colors = MaterialTheme.colorScheme.extended.reflectionCard

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(Brush.linearGradient(colors.surfaceGradient))
            .border(1.dp, colors.border, RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .padding(MaterialTheme.dimens.spacing.xl)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.tile)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                    .background(Brush.linearGradient(listOf(colors.iconStart, colors.iconEnd)))
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                )
            }
            Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.ai_analysis_title),
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
                    modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xxs)
                )
            }
        }

        Text(
            text = reflection.message,
            fontSize = 14.5.sp,
            lineHeight = 23.5.sp,
            fontWeight = FontWeight.Medium,
            color = colors.body,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
        )

        if (reflection.analysis.themes.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
            ) {
                reflection.analysis.themes.forEachIndexed { index, theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                        modifier = Modifier
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                            .background(colors.chipBg)
                            .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.sm)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(ReflectionChipDots[index % ReflectionChipDots.size])
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
                    .padding(top = MaterialTheme.dimens.spacing.lg)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                    .background(colors.promptBg)
                    .border(1.dp, colors.promptBorder, RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
            ) {
                Text(
                    text = stringResource(Res.string.ai_question_to_sit_with),
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
                    modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xs)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = MaterialTheme.dimens.spacing.lg)
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.hairline)
        )
        Text(
            text = stringResource(Res.string.ai_reflection_disclaimer),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.disclaimer,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.md)
        )
    }
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
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}

@Preview
@Composable
private fun EntryAnalysisCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryAnalysisCard(
            reflection = sampleReflection(),
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
