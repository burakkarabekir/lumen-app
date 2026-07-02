package com.bksd.journal.presentation.detail.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.ui.graphics.vector.ImageVector
import com.bksd.core.domain.model.Mood

internal fun detailMoodIcon(mood: Mood): ImageVector = when (mood) {
    Mood.HAPPY -> Icons.Filled.SentimentVerySatisfied
    Mood.GRATEFUL -> Icons.Filled.VolunteerActivism
    Mood.CALM -> Icons.Filled.Spa
    Mood.LOVED -> Icons.Filled.Favorite
    Mood.HOPEFUL -> Icons.Filled.WbTwilight
    Mood.PROUD -> Icons.Filled.EmojiEvents
    Mood.INSPIRED -> Icons.Filled.AutoAwesome
    Mood.REFLECTIVE -> Icons.Filled.Psychology
    Mood.FOCUSED -> Icons.Filled.GpsFixed
    Mood.NOSTALGIC -> Icons.Filled.PhotoCamera
    Mood.TIRED -> Icons.Filled.Bedtime
    Mood.ANXIOUS -> Icons.Filled.MonitorHeart
    Mood.STRESSED -> Icons.Filled.Bolt
    Mood.FRUSTRATED -> Icons.Filled.LocalFireDepartment
    Mood.SAD -> Icons.Filled.SentimentDissatisfied
    Mood.MELANCHOLIC -> Icons.Filled.Cloud
    Mood.BORED -> Icons.Filled.SentimentNeutral
}
