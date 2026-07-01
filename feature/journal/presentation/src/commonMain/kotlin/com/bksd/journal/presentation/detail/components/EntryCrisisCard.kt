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
import androidx.compose.material.icons.filled.Favorite
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
fun EntryCrisisCard(
    reflection: MomentReflection.Crisis,
    modifier: Modifier = Modifier,
) {
    val dark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val iconStart = Color(0xFFE58060)
    val iconEnd = Color(0xFFCD5A48)
    val accent = if (dark) Color(0xFFF0A08F) else Color(0xFFB5473A)
    val surface = if (dark) Color(0xFF2C1C18) else Color(0xFFFBEDE7)
    val border = if (dark) Color.White.copy(alpha = 0.07f) else Color(0xFFCD5A48).copy(alpha = 0.24f)
    val title = if (dark) Color.White else Color(0xFF4A1E1A)
    val body = if (dark) Color.White.copy(alpha = 0.88f) else Color(0xFF43302B)
    val rowBg = if (dark) Color.White.copy(alpha = 0.06f) else Color.White

    val resources = buildList {
        add(reflection.emergency)
        addAll(reflection.crisisLines)
    }

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
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(11.dp))
            Text(
                text = "You're not alone",
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

        Column(
            verticalArrangement = Arrangement.spacedBy(9.dp),
            modifier = Modifier.padding(top = 15.dp)
        ) {
            resources.forEach { resource ->
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

@Preview
@Composable
private fun EntryCrisisCardPreview() {
    AppTheme {
        EntryCrisisCard(
            reflection = MomentReflection.Crisis(
                analysis = EntryAnalysis(
                    summary = "Crisis.",
                    moodValence = MoodValence.VERY_LOW,
                    moodConfidence = 0.9,
                    dominantEmotions = listOf("despair"),
                    themes = emptyList(),
                    distress = com.bksd.reflection.domain.model.DistressLevel.CRISIS,
                    distressRationale = ""
                ),
                message = "If you're thinking about harming yourself or feel unsafe, please reach out for " +
                    "help right now. You deserve support, and people are available to talk with you.",
                emergency = SupportResource("Emergency", "112"),
                crisisLines = emptyList()
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
