package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme

@Composable
fun EntryAnalysisLoadingCard(
    modifier: Modifier = Modifier,
) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val surface = if (dark) Color.White.copy(alpha = 0.05f) else Color(0xFF4F46E5).copy(alpha = 0.05f)
    val border = if (dark) Color.White.copy(alpha = 0.08f) else Color(0xFF4F46E5).copy(alpha = 0.13f)
    val title = if (dark) Color.White else Color(0xFF22203A)
    val sub = if (dark) Color.White.copy(alpha = 0.5f) else Color(0xFF8A867F)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(surface)
            .border(1.dp, border, RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF7682D6), Color(0xFFCF6F64))))
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = "Analyzing your entry…",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = title
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = "Your reflection will appear here in a moment.",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = sub
            )
        }
        Spacer(Modifier.width(12.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 2.dp,
            color = Color(0xFF7682D6)
        )
    }
}

@Preview
@Composable
private fun EntryAnalysisLoadingCardPreview() {
    AppTheme {
        EntryAnalysisLoadingCard(modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun EntryAnalysisLoadingCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryAnalysisLoadingCard(modifier = Modifier.padding(16.dp))
    }
}
