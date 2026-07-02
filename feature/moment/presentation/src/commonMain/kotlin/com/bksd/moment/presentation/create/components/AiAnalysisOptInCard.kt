package com.bksd.moment.presentation.create.components

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import androidx.compose.material3.MaterialTheme
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.aiIconGradient
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.moment.presentation.Res
import com.bksd.moment.presentation.ai_optin_caption
import com.bksd.moment.presentation.ai_optin_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun AiAnalysisOptInCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg

    val background = if (checked) accent.copy(alpha = 0.08f) else palette.surface
    val borderColor = if (checked) accent.copy(alpha = 0.55f) else palette.hairline

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .background(background)
            .border(1.5.dp, borderColor, RoundedCornerShape(MaterialTheme.dimens.radius.xl))
            .clickable { onCheckedChange(!checked) }
            .padding(MaterialTheme.dimens.spacing.lg)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.tile)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                .background(
                    Brush.linearGradient(MaterialTheme.colorScheme.extended.aiIconGradient)
                )
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.ai_optin_title),
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = palette.text
            )
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxs))
            Text(
                text = stringResource(Res.string.ai_optin_caption),
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = palette.sub
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.xl)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                .background(if (checked) accent else Color.Transparent)
                .border(
                    2.dp,
                    if (checked) accent else palette.sub,
                    RoundedCornerShape(MaterialTheme.dimens.radius.sm)
                )
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AiAnalysisOptInCardCheckedPreview() {
    AppTheme {
        AiAnalysisOptInCard(
            checked = true,
            onCheckedChange = {},
            modifier = Modifier.width(360.dp)
        )
    }
}

@Preview
@Composable
private fun AiAnalysisOptInCardUncheckedPreview() {
    AppTheme {
        AiAnalysisOptInCard(
            checked = false,
            onCheckedChange = {},
            modifier = Modifier.width(360.dp)
        )
    }
}
