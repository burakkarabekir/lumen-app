package com.bksd.moment.presentation.create.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import com.bksd.core.design_system.theme.rememberNewEntryPalette

@Composable
fun AiAnalysisOptInCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    val accent = palette.saveBg

    val background = if (checked) accent.copy(alpha = 0.08f) else palette.surface
    val borderColor = if (checked) accent.copy(alpha = 0.55f) else palette.hairline

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(background)
            .border(1.5.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(15.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF7682D6), Color(0xFFCF6F64))
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(21.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Yes, analyze this entry with AI",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = palette.text
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Get a reflection on your mood and themes right after you save.",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = palette.sub
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(if (checked) accent else Color.Transparent)
                .border(
                    2.dp,
                    if (checked) accent else palette.sub,
                    RoundedCornerShape(9.dp)
                )
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AiAnalysisOptInCardCheckedPreview() {
    AppTheme {
        AiAnalysisOptInCard(
            checked = true,
            onCheckedChange = {},
            modifier = Modifier.width(360.dp)
        )
    }
}

@Preview
@Composable
private fun AiAnalysisOptInCardUncheckedPreview() {
    AppTheme {
        AiAnalysisOptInCard(
            checked = false,
            onCheckedChange = {},
            modifier = Modifier.width(360.dp)
        )
    }
}
