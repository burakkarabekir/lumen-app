package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.extended
import com.bksd.core.domain.model.MediaType
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.model.Mood

@Composable
fun MomentCard(
    moment: Moment,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            // Header: Time and Mood tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Formatting time explicitly since we only have timestamp
                Text(
                    text = "10:45 AM", // Mocked formatting for now
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                MoodTag(mood = moment.mood)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Body replaces Title
            val safeBody = moment.body ?: ""
            val displayText = safeBody.take(50).let { if (safeBody.length > 50) "$it..." else it }
            Text(
                text = displayText.ifEmpty { "Empty Moment" },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Body
            val body = moment.body
            if (body != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = body,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Media
            val attachments = moment.attachments
            if (attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                MediaPlaceholder(type = attachments.first().type)
            }

            // Tags
            if (moment.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    moment.tags.forEach { tag ->
                        Text(
                            text = "#$tag",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoodTag(mood: Mood) {
    val extendedColors = MaterialTheme.colorScheme.extended
    val (backgroundColor, textColor) = when (mood) {
        Mood.ENERGETIC -> extendedColors.emotionJoyBg to extendedColors.emotionJoy
        Mood.REFLECTIVE -> extendedColors.emotionSurpriseBg to extendedColors.emotionSurprise
        Mood.CALM -> extendedColors.emotionCalmBg to extendedColors.emotionCalm
        Mood.TIRED -> extendedColors.emotionSadnessBg to extendedColors.emotionSadness
        Mood.INSPIRED -> extendedColors.emotionGratitudeBg to extendedColors.emotionGratitude
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = mood.label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun MediaPlaceholder(type: MediaType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (type == MediaType.PHOTO) "[Photo Placeholder]" else "[Audio Waveform Player]",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
