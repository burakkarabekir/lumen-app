package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme

@Composable
fun EditSectionLabel(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.7.sp,
        color = color,
        modifier = modifier.padding(start = 2.dp, bottom = 8.dp)
    )
}

@Preview
@Composable
private fun EditSectionLabelPreview() {
    AppTheme(darkTheme = true) {
        EditSectionLabel(
            text = "DATE & TIME",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
