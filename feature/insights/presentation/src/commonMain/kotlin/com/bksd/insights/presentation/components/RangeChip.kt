package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme

@Composable
internal fun RangeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(if (selected) Color.White.copy(alpha = 0.22f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.55f)
        )
    }
}

@Preview
@Composable
private fun RangeChipPreview() {
    AppTheme {
        Row(
            modifier = Modifier.background(Color(0xFF7682D6)).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            RangeChip(label = "All-time", selected = true, onClick = {})
            RangeChip(label = "2025", selected = false, onClick = {})
        }
    }
}
