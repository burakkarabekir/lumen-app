package com.bksd.core.domain.notification

import com.bksd.core.domain.reminder.ReminderSettings

interface ReminderScheduler {
    suspend fun reschedule(settings: ReminderSettings)
    fun hasNotificationPermission(): Boolean
}
