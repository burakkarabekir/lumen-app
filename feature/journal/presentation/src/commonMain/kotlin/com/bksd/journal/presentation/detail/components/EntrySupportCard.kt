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
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.bksd.journal.presentation.ai_support_title
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.model.MoodValence
import com.bksd.reflection.domain.support.SupportResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EntrySupportCard(
    reflection: MomentReflection.Support,
    modifier: Modifier = Modifier,
) {
    val c = MaterialTheme.colorScheme.extended.supportCard

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
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                )
            }
            Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
            Text(
                text = stringResource(Res.string.ai_support_title),
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

        if (reflection.mentalHealthLines.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
            ) {
                reflection.mentalHealthLines.forEach { resource ->
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
}

@Preview
@Composable
private fun EntrySupportCardPreview() {
    AppTheme {
        EntrySupportCard(
            reflection = MomentReflection.Support(
                analysis = EntryAnalysis(
                    summary = "Heavy day.",
                    moodValence = MoodValence.LOW,
                    moodConfidence = 0.7,
                    dominantEmotions = listOf("overwhelm"),
                    themes = listOf("stress"),
                    distress = com.bksd.reflection.domain.model.DistressLevel.ELEVATED,
                    distressRationale = ""
                ),
                message = "It sounds like things feel heavy right now, and that's hard. You don't have " +
                    "to carry it alone — reaching out to someone you trust can make a difference.",
                mentalHealthLines = listOf(SupportResource("Support line", "0212 000 0000"))
            ),
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
