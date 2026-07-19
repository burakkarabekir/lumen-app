package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.analyze_prompt_body
import com.bksd.journal.presentation.analyze_prompt_button
import com.bksd.journal.presentation.analyze_prompt_title
import org.jetbrains.compose.resources.stringResource

private val OrbHighlight = Color(0xFFDCE3FF)
private val OrbMid = Color(0xFF5B6AD8)
private val OrbDeep = Color(0xFF33308F)
private val AnalyzeAccent = Color(0xFFCF6F64)

@Composable
fun EntryAnalyzePromptCard(
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                RoundedCornerShape(MaterialTheme.dimens.radius.card),
            )
            .padding(MaterialTheme.dimens.spacing.xl),
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.0f to OrbHighlight,
                                0.45f to OrbMid,
                                1.0f to OrbDeep,
                            ),
                            center = Offset(size.width * 0.34f, size.height * 0.30f),
                            radius = size.width * 0.92f,
                        ),
                    )
                },
        )
        Text(
            text = stringResource(Res.string.analyze_prompt_title),
            fontSize = 20.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.3).sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg),
        )
        Text(
            text = stringResource(Res.string.analyze_prompt_body),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.sm),
        )
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.full))
                    .background(AnalyzeAccent)
                    .clickable(role = Role.Button, onClick = onAnalyze)
                    .padding(
                        horizontal = MaterialTheme.dimens.spacing.xl,
                        vertical = 14.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
            ) {
                Text(
                    text = stringResource(Res.string.analyze_prompt_button),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.sm),
                )
            }
        }
    }
}

@Preview
@Composable
private fun EntryAnalyzePromptCardPreview() {
    AppTheme {
        EntryAnalyzePromptCard(
            onAnalyze = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg),
        )
    }
}

@Preview
@Composable
private fun EntryAnalyzePromptCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryAnalyzePromptCard(
            onAnalyze = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg),
        )
    }
}
