package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme

@Composable
internal fun RowScope.MiniStat(value: String, label: String) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text(
            text = label,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Preview
@Composable
private fun MiniStatPreview() {
    AppTheme {
        Box(Modifier.background(Color(0xFFCF524B)).padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                MiniStat("1", "This Month")
                MiniStat("34", "This Year")
            }
        }
    }
}
