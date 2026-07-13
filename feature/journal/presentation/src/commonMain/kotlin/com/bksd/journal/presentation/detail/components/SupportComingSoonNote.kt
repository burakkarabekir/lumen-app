package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.ai_support_coming_soon
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SupportComingSoonNote(
    textColor: Color,
    background: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(Res.string.ai_support_coming_soon),
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
            .background(background)
            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
    )
}

@Preview
@Composable
private fun SupportComingSoonNotePreview() {
    AppTheme {
        SupportComingSoonNote(
            textColor = Color(0xFF22203A),
            background = Color.White,
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
