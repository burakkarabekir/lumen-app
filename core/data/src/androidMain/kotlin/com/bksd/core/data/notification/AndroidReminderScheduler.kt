package com.bksd.core.data.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bksd.core.domain.notification.ReminderScheduler
import com.bksd.core.domain.notification.ReminderTextProvider
import com.bksd.core.domain.reminder.ReminderSettings
import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

private const val STREAK_REQUEST_CODE = 100
private const val STREAK_HOUR = 21
private const val REMINDER_WINDOW_MILLIS = 5 * 60 * 1000L
private val ALL_REQUEST_CODES = (1..7) + STREAK_REQUEST_CODE

class AndroidReminderScheduler(
    private val context: Context,
    private val textProvider: ReminderTextProvider
) : ReminderScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override suspend fun reschedule(settings: ReminderSettings) {
        cancelAll()
        val texts = textProvider.reminderTexts()
        if (settings.dailyEnabled) {
            settings.days.forEach { day ->
                schedule(
                    requestCode = day,
                    triggerAtMillis = nextWeekly(day, settings.hour, settings.minute),
                    title = texts.dailyTitle,
                    body = texts.dailyBody
                )
            }
        }
        if (settings.streakEnabled) {
            schedule(
                requestCode = STREAK_REQUEST_CODE,
                triggerAtMillis = nextDaily(STREAK_HOUR, 0),
                title = texts.streakTitle,
                body = texts.streakBody
            )
        }
    }

    override fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    private fun schedule(
        requestCode: Int,
        triggerAtMillis: Long,
        title: String,
        body: String
    ) {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_ID, requestCode)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_BODY, body)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            REMINDER_WINDOW_MILLIS,
            pendingIntent
        )
    }

    private fun cancelAll() {
        ALL_REQUEST_CODES.forEach { requestCode ->
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, ReminderBroadcastReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    private fun nextWeekly(isoDay: Int, hour: Int, minute: Int): Long {
        val now = ZonedDateTime.now()
        var next = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(isoDay)))
            .withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusWeeks(1)
        return next.toInstant().toEpochMilli()
    }

    private fun nextDaily(hour: Int, minute: Int): Long {
        val now = ZonedDateTime.now()
        var next = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0)
        if (!next.isAfter(now)) next = next.plusDays(1)
        return next.toInstant().toEpochMilli()
    }
}
