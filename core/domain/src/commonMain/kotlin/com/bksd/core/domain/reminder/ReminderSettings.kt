package com.bksd.core.domain.reminder

data class ReminderSettings(
    val dailyEnabled: Boolean = false,
    val hour: Int = 20,
    val minute: Int = 0,
    val days: Set<Int> = ALL_DAYS,
    val streakEnabled: Boolean = false,
) {
    companion object {
        val ALL_DAYS = setOf(1, 2, 3, 4, 5, 6, 7)
        val Default = ReminderSettings()
    }
}
