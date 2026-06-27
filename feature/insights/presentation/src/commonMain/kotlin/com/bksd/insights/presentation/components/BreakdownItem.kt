package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.insights.presentation.StatItem

@Composable
internal fun RowScope.BreakdownItem(item: StatItem) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${item.value}",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = item.label,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.72f),
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Preview
@Composable
private fun BreakdownItemPreview() {
    AppTheme {
        Box(Modifier.background(Color(0xFF7682D6)).padding(16.dp)) {
            Row { BreakdownItem(StatItem(51, "Photos")) }
        }
    }
}
