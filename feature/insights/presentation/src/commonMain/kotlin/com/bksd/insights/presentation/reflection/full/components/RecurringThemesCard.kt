package com.bksd.insights.presentation.reflection.full.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.reflection.reflectionHexColor
import com.bksd.insights.presentation.weekly_entry_count
import com.bksd.insights.presentation.weekly_entry_plural
import com.bksd.insights.presentation.weekly_entry_singular
import com.bksd.reflection.domain.model.ReflectionTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecurringThemesCard(
    themes: ImmutableList<ReflectionTheme>,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val trackColor = if (dark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.06f)
    val maxCount = themes.maxOfOrNull { it.count }?.coerceAtLeast(1) ?: 1

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(palette.surface)
    ) {
        themes.forEachIndexed { index, theme ->
            val color = reflectionHexColor(theme.colorHex)
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        Text(
                            text = theme.label,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = palette.text
                        )
                    }
                    val entryNoun = stringResource(
                        if (theme.count == 1) Res.string.weekly_entry_singular
                        else Res.string.weekly_entry_plural
                    )
                    Text(
                        text = stringResource(Res.string.weekly_entry_count, theme.count, entryNoun),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = palette.sub
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.spacing.md)
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xs))
                        .background(trackColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(theme.count.toFloat() / maxCount)
                            .height(6.dp)
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xs))
                            .background(color)
                    )
                }
            }
            if (index < themes.lastIndex) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(palette.hairline))
            }
        }
    }
}

@Preview
@Composable
private fun RecurringThemesCardPreview() {
    AppTheme(darkTheme = true) {
        RecurringThemesCard(
            themes = listOf(
                ReflectionTheme("Calm", "#2FA876", 5),
                ReflectionTheme("Gratitude", "#C77FA8", 3),
                ReflectionTheme("Mornings", "#E0A21A", 3),
                ReflectionTheme("Rest", "#6E7AD0", 2),
            ).toImmutableList(),
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
