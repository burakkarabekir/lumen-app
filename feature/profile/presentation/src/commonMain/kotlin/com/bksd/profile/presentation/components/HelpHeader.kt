package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.help_intro_body
import com.bksd.profile.presentation.help_intro_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HelpHeader(
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.dimens.spacing.lg, bottom = MaterialTheme.dimens.spacing.sm)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.dimens.size.topBar)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.14f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Help,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(MaterialTheme.dimens.icon.avatar)
            )
        }
        Text(
            text = stringResource(Res.string.help_intro_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
            color = palette.text,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg)
        )
        Text(
            text = stringResource(Res.string.help_intro_body),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = palette.bodyText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.sm, start = MaterialTheme.dimens.spacing.xxxl, end = MaterialTheme.dimens.spacing.xxxl)
        )
    }
}

@Preview
@Composable
private fun HelpHeaderPreview() {
    PreviewAppTheme(darkTheme = true) {
        HelpHeader()
    }
}
