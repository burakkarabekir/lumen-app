package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.model.MoodValence
import com.bksd.reflection.domain.support.SupportResource

@Composable
fun EntrySupportCard(
    reflection: MomentReflection.Support,
    modifier: Modifier = Modifier,
) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val iconStart = Color(0xFF57B98F)
    val iconEnd = Color(0xFF3E9B77)
    val accent = if (dark) Color(0xFF8FD8B4) else Color(0xFF2E7D57)
    val surface = if (dark) Color(0xFF18291F) else Color(0xFFE9F4ED)
    val border = if (dark) Color.White.copy(alpha = 0.07f) else Color(0xFF3E9B77).copy(alpha = 0.24f)
    val title = if (dark) Color.White else Color(0xFF16321F)
    val body = if (dark) Color.White.copy(alpha = 0.87f) else Color(0xFF39463F)
    val rowBg = if (dark) Color.White.copy(alpha = 0.06f) else Color.White

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(surface)
            .border(1.dp, border, RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(iconStart, iconEnd)))
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(11.dp))
            Text(
                text = "A moment of support",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.2).sp,
                color = title
            )
        }

        Text(
            text = reflection.message,
            fontSize = 14.5.sp,
            lineHeight = 23.5.sp,
            fontWeight = FontWeight.Medium,
            color = body,
            modifier = Modifier.padding(top = 15.dp)
        )

        if (reflection.mentalHealthLines.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(9.dp),
                modifier = Modifier.padding(top = 15.dp)
            ) {
                reflection.mentalHealthLines.forEach { resource ->
                    SupportResourceRow(
                        resource = resource,
                        accent = accent,
                        rowBackground = rowBg,
                        labelColor = title
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EntrySupportCardPreview() {
    AppTheme {
        EntrySupportCard(
            reflection = MomentReflection.Support(
                analysis = EntryAnalysis(
                    summary = "Heavy day.",
                    moodValence = MoodValence.LOW,
                    moodConfidence = 0.7,
                    dominantEmotions = listOf("overwhelm"),
                    themes = listOf("stress"),
                    distress = com.bksd.reflection.domain.model.DistressLevel.ELEVATED,
                    distressRationale = ""
                ),
                message = "It sounds like things feel heavy right now, and that's hard. You don't have " +
                    "to carry it alone — reaching out to someone you trust can make a difference.",
                mentalHealthLines = listOf(SupportResource("Support line", "0212 000 0000"))
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
