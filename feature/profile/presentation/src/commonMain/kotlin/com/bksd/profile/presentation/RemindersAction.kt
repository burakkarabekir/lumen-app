package com.bksd.profile.presentation

sealed interface RemindersAction {
    data class OnDailyToggle(val enabled: Boolean) : RemindersAction
    data class OnTimeSelected(val hour: Int, val minute: Int) : RemindersAction
    data class OnDayToggle(val day: Int) : RemindersAction
    data class OnStreakToggle(val enabled: Boolean) : RemindersAction
}
