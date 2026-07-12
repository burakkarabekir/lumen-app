package com.bksd.core.domain.notification

data class ReminderTexts(
    val dailyTitle: String,
    val dailyBody: String,
    val streakTitle: String,
    val streakBody: String,
)

interface ReminderTextProvider {
    suspend fun reminderTexts(): ReminderTexts
}
