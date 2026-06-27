package com.bksd.insights.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.bksd.core.design_system.theme.AppTheme

@Composable
internal fun WrittenCard(words: Int, modifier: Modifier) {
    Column(
        modifier = modifier
            .height(StatCardHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFFD3796A), Color(0xFFC0584F))))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Written",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = words.grouped(),
            fontSize = 40.sp,
            lineHeight = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "Words",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

private fun Int.grouped(): String =
    toString().reversed().chunked(3).joinToString(",").reversed()

@Preview
@Composable
private fun WrittenCardPreview() {
    AppTheme {
        Box(Modifier.padding(18.dp).width(170.dp)) {
            WrittenCard(words = 4930, modifier = Modifier)
        }
    }
}
