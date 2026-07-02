package com.bksd.insights.presentation.reflection.full.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.reflection.reflectionHexColor
import com.bksd.insights.presentation.weekly_open
import com.bksd.reflection.domain.model.StandoutEntry
import org.jetbrains.compose.resources.stringResource

@Composable
fun StandoutMomentCard(
    standout: StandoutEntry,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(palette.surface)
            .clickable(onClick = onOpen)
            .padding(MaterialTheme.dimens.spacing.xl)
    ) {
        Text(
            text = "“",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = accent.copy(alpha = 0.20f),
            modifier = Modifier.align(Alignment.TopStart)
        )
        Column {
            Text(
                text = standout.quote,
                fontSize = 16.sp,
                lineHeight = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = palette.text,
                modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.xl, top = MaterialTheme.dimens.spacing.xs)
            )
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.lg))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(palette.hairline))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.dimens.spacing.md)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm)
                ) {
                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.dimens.icon.xl)
                            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                            .background(reflectionHexColor(standout.colorHex))
                    )
                    Text(
                        text = standout.title,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = palette.text
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.weekly_open),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = accent
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun StandoutMomentCardPreview() {
    AppTheme(darkTheme = true) {
        StandoutMomentCard(
            standout = StandoutEntry(
                momentId = "1",
                title = "Morning pages",
                quote = "I want more mornings that start this slowly.",
                colorHex = "#C77FA8"
            ),
            onOpen = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
