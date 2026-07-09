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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.reflection.reflectionHexColor
import com.bksd.insights.presentation.weekly_based_on_entries
import com.bksd.insights.presentation.weekly_entry_plural
import com.bksd.insights.presentation.weekly_entry_singular
import com.bksd.insights.presentation.weekly_private_to_you
import com.bksd.insights.presentation.weekly_question_to_sit_with
import com.bksd.insights.presentation.weekly_reflection_title
import com.bksd.insights.presentation.weekly_view_full
import com.bksd.reflection.domain.model.ReflectionTheme
import com.bksd.reflection.domain.model.WeeklyReflection
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeeklyReflectionCard(
    reflection: WeeklyReflection,
    onViewFull: () -> Unit,
    modifier: Modifier = Modifier
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
                    text = stringResource(Res.string.weekly_reflection_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.2).sp,
                    color = colors.title
                )
                val entryNoun = stringResource(
                    if (reflection.entryCount == 1) Res.string.weekly_entry_singular
                    else Res.string.weekly_entry_plural
                )
                Text(
                    text = stringResource(
                        Res.string.weekly_based_on_entries,
                        reflection.entryCount,
                        entryNoun,
                        reflection.rangeLabel
                    ),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.meta,
                    modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.xxs)
                )
            }
            if (reflection.summary.isNotBlank()) {
                Spacer(Modifier.width(MaterialTheme.dimens.spacing.sm))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
                    modifier = Modifier
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                        .background(colors.pillBg)
                        .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.xs)
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
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
        )

        if (reflection.themes.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
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
                    .padding(top = MaterialTheme.dimens.spacing.lg)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                    .background(colors.promptBg)
                    .border(1.dp, colors.promptBorder, RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
            ) {
                Text(
                    text = stringResource(Res.string.weekly_question_to_sit_with),
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = MaterialTheme.dimens.spacing.lg)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                    .clickable(onClick = onViewFull)
            ) {
                Text(
                    text = stringResource(Res.string.weekly_view_full),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.title
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = colors.title,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                )
            }
            Text(
                text = stringResource(Res.string.weekly_private_to_you),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.meta
            )
        }
    }
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
        WeeklyReflectionCard(reflection = sampleWeekly(), onViewFull = {}, modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg))
    }
}

@Preview
@Composable
private fun WeeklyReflectionCardDarkPreview() {
    AppTheme(darkTheme = true) {
        WeeklyReflectionCard(reflection = sampleWeekly(), onViewFull = {}, modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg))
    }
}
