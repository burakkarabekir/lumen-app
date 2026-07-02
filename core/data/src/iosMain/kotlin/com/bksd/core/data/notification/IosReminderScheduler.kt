package com.bksd.core.data.notification

import com.bksd.core.domain.notification.ReminderScheduler
import com.bksd.core.domain.reminder.ReminderSettings
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

class IosReminderScheduler : ReminderScheduler {

    private val center = UNUserNotificationCenter.currentNotificationCenter()

    override suspend fun reschedule(settings: ReminderSettings) {
        center.removeAllPendingNotificationRequests()
        if (settings.dailyEnabled) {
            settings.days.forEach { isoDay ->
                schedule(
                    identifier = "daily_$isoDay",
                    weekday = isoToAppleWeekday(isoDay),
                    hour = settings.hour,
                    minute = settings.minute,
                    title = "Time to journal",
                    body = "Take a moment to capture your day."
                )
            }
        }
        if (settings.streakEnabled) {
            schedule(
                identifier = "streak",
                weekday = null,
                hour = 21,
                minute = 0,
                title = "Don't break your streak",
                body = "Write an entry to keep your streak alive."
            )
        }
    }

    override fun hasNotificationPermission(): Boolean = true

    private fun schedule(
        identifier: String,
        weekday: Int?,
        hour: Int,
        minute: Int,
        title: String,
        body: String
    ) {
        val content = UNMutableNotificationContent()
        content.setTitle(title)
        content.setBody(body)
        content.setSound(UNNotificationSound.defaultSound)

        val components = NSDateComponents()
        components.hour = hour.toLong()
        components.minute = minute.toLong()
        if (weekday != null) components.weekday = weekday.toLong()

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = components,
            repeats = true
        )
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = identifier,
            content = content,
            trigger = trigger
        )
        center.addNotificationRequest(request, null)
    }

    private fun isoToAppleWeekday(isoDay: Int): Int = (isoDay % 7) + 1
}
