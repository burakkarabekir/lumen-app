package com.bksd.insights.presentation.reflection.full.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette

@Composable
fun WeeklySectionLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.7.sp,
        color = palette.sub,
        modifier = modifier.padding(start = MaterialTheme.dimens.spacing.xxs)
    )
}

@Preview
@Composable
private fun WeeklySectionLabelPreview() {
    AppTheme {
        WeeklySectionLabel(text = "RECURRING THEMES", modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg))
    }
}
