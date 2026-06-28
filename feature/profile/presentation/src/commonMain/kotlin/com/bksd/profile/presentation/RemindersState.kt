package com.bksd.profile.presentation

import com.bksd.core.domain.reminder.ReminderSettings

data class RemindersState(
    val dailyEnabled: Boolean = false,
    val hour: Int = 20,
    val minute: Int = 0,
    val days: Set<Int> = ReminderSettings.ALL_DAYS,
    val streakEnabled: Boolean = false,
    val loaded: Boolean = false,
) {
    fun toSettings(): ReminderSettings = ReminderSettings(
        dailyEnabled = dailyEnabled,
        hour = hour,
        minute = minute,
        days = days,
        streakEnabled = streakEnabled,
    )
}
