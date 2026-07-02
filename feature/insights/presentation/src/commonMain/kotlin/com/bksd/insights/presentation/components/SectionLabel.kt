package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.InsightsPalette
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberInsightsPalette

@Composable
internal fun SectionLabel(text: String, palette: InsightsPalette) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.2.sp,
        color = palette.label,
        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.spacing.xxs)
    )
}

@Preview
@Composable
private fun SectionLabelPreview() {
    AppTheme {
        val palette = rememberInsightsPalette()
        Box(Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)) {
            SectionLabel("Streaks", palette)
        }
    }
}
