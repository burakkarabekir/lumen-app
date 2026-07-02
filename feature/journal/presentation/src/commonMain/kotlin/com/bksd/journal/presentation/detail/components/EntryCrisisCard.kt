package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.ai_crisis_title
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.model.MoodValence
import com.bksd.reflection.domain.support.SupportResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EntryCrisisCard(
    reflection: MomentReflection.Crisis,
    modifier: Modifier = Modifier,
) {
    val c = MaterialTheme.colorScheme.extended.crisisCard
    val resources = buildList {
        add(reflection.emergency)
        addAll(reflection.crisisLines)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(Brush.linearGradient(c.surfaceGradient))
            .border(1.dp, c.border, RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .padding(MaterialTheme.dimens.spacing.xl)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.tile)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                    .background(Brush.linearGradient(listOf(c.iconStart, c.iconEnd)))
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                )
            }
            Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
            Text(
                text = stringResource(Res.string.ai_crisis_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.2).sp,
                color = c.title
            )
        }

        Text(
            text = reflection.message,
            fontSize = 14.5.sp,
            lineHeight = 23.5.sp,
            fontWeight = FontWeight.Medium,
            color = c.body,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
        ) {
            resources.forEach { resource ->
                SupportResourceRow(
                    resource = resource,
                    accent = c.accent,
                    rowBackground = c.rowBg,
                    labelColor = c.title
                )
            }
        }
    }
}

@Preview
@Composable
private fun EntryCrisisCardPreview() {
    AppTheme {
        EntryCrisisCard(
            reflection = MomentReflection.Crisis(
                analysis = EntryAnalysis(
                    summary = "Crisis.",
                    moodValence = MoodValence.VERY_LOW,
                    moodConfidence = 0.9,
                    dominantEmotions = listOf("despair"),
                    themes = emptyList(),
                    distress = com.bksd.reflection.domain.model.DistressLevel.CRISIS,
                    distressRationale = ""
                ),
                message = "If you're thinking about harming yourself or feel unsafe, please reach out for " +
                    "help right now. You deserve support, and people are available to talk with you.",
                emergency = SupportResource("Emergency", "112"),
                crisisLines = emptyList()
            ),
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
