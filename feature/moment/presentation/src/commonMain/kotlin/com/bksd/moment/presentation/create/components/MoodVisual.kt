package com.bksd.moment.presentation.create.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.bksd.core.domain.model.Mood

data class MoodVisual(val icon: ImageVector, val hue: Color)

fun moodVisual(mood: Mood): MoodVisual = when (mood) {
    Mood.HAPPY -> MoodVisual(Icons.Filled.SentimentVerySatisfied, Color(0xFFE0A21A))
    Mood.GRATEFUL -> MoodVisual(Icons.Filled.VolunteerActivism, Color(0xFFC77FA8))
    Mood.CALM -> MoodVisual(Icons.Filled.Spa, Color(0xFF2FA876))
    Mood.LOVED -> MoodVisual(Icons.Filled.Favorite, Color(0xFFD9534A))
    Mood.HOPEFUL -> MoodVisual(Icons.Filled.WbTwilight, Color(0xFF3F9C8D))
    Mood.PROUD -> MoodVisual(Icons.Filled.EmojiEvents, Color(0xFFF2932B))
    Mood.INSPIRED -> MoodVisual(Icons.Filled.AutoAwesome, Color(0xFF8A6FBF))
    Mood.REFLECTIVE -> MoodVisual(Icons.Filled.Psychology, Color(0xFF6E7AD0))
    Mood.FOCUSED -> MoodVisual(Icons.Filled.GpsFixed, Color(0xFF5E9FD6))
    Mood.NOSTALGIC -> MoodVisual(Icons.Filled.PhotoCamera, Color(0xFFB0815B))
    Mood.TIRED -> MoodVisual(Icons.Filled.Bedtime, Color(0xFF6E6C9E))
    Mood.ANXIOUS -> MoodVisual(Icons.Filled.MonitorHeart, Color(0xFFC08A1E))
    Mood.STRESSED -> MoodVisual(Icons.Filled.Bolt, Color(0xFFCF6F64))
    Mood.FRUSTRATED -> MoodVisual(Icons.Filled.LocalFireDepartment, Color(0xFFC5453B))
    Mood.SAD -> MoodVisual(Icons.Filled.SentimentDissatisfied, Color(0xFF5E72C8))
    Mood.MELANCHOLIC -> MoodVisual(Icons.Filled.Cloud, Color(0xFF9CA3AF))
    Mood.BORED -> MoodVisual(Icons.Filled.SentimentNeutral, Color(0xFF8E949B))
}
