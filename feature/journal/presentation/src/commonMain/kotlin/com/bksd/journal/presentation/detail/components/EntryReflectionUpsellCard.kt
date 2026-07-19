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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.upsell_reflections_body
import com.bksd.journal.presentation.upsell_reflections_title
import com.bksd.journal.presentation.upsell_unlock_plus
import org.jetbrains.compose.resources.stringResource

private val UnlockGradientStart = Color(0xFF7C7FE8)
private val UnlockGradientEnd = Color(0xFF5B4FE0)

@Composable
fun EntryReflectionUpsellCard(
    onUnlock: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = MaterialTheme.colorScheme.extended.reflectionCard
    val cardShape = RoundedCornerShape(MaterialTheme.dimens.radius.card)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Brush.linearGradient(c.surfaceGradient))
            .border(1.dp, c.border, cardShape),
    ) {
        Column(
            modifier = Modifier
                .matchParentSize()
                .padding(MaterialTheme.dimens.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
        ) {
            Spacer(Modifier.height(62.dp))
            listOf(0.82f, 0.64f, 0.74f, 0.48f).forEach { fraction ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .height(10.dp)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                        .background(c.body.copy(alpha = 0.12f)),
                )
            }
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xs))
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)) {
                listOf(58.dp, 46.dp).forEach { chipWidth ->
                    Box(
                        modifier = Modifier
                            .width(chipWidth)
                            .height(18.dp)
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                            .background(c.chipBg),
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            c.surfaceGradient.first().copy(alpha = 0.55f),
                            Color.Transparent,
                            c.surfaceGradient.last().copy(alpha = 0.85f),
                        ),
                    ),
                ),
        )

        Column(
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.xl),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(UnlockGradientStart, UnlockGradientEnd))),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.xl),
                )
            }
            Text(
                text = stringResource(Res.string.upsell_reflections_title),
                fontSize = 20.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.3).sp,
                color = c.title,
                modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg),
            )
            Text(
                text = stringResource(Res.string.upsell_reflections_body),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = c.body,
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
                        .background(Brush.linearGradient(listOf(UnlockGradientStart, UnlockGradientEnd)))
                        .clickable(role = Role.Button, onClick = onUnlock)
                        .padding(
                            horizontal = MaterialTheme.dimens.spacing.xl,
                            vertical = 14.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                ) {
                    Text(
                        text = stringResource(Res.string.upsell_unlock_plus),
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
}

@Preview
@Composable
private fun EntryReflectionUpsellCardPreview() {
    AppTheme {
        EntryReflectionUpsellCard(
            onUnlock = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg),
        )
    }
}

@Preview
@Composable
private fun EntryReflectionUpsellCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryReflectionUpsellCard(
            onUnlock = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg),
        )
    }
}
