package com.bksd.core.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val REMINDER_CHANNEL_ID = "lumen_reminders"

internal const val EXTRA_ID = "reminder_id"
internal const val EXTRA_TITLE = "reminder_title"
internal const val EXTRA_BODY = "reminder_body"

fun createReminderChannel(context: Context) {
    val channel = NotificationChannel(
        REMINDER_CHANNEL_ID,
        "Reminders",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Journaling reminders"
    }
    context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
}

internal fun postReminderNotification(context: Context, id: Int, title: String, body: String) {
    createReminderChannel(context)
    val manager = NotificationManagerCompat.from(context)
    if (!manager.areNotificationsEnabled()) return
    val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_popup_reminder)
        .setContentTitle(title)
        .setContentText(body)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
    try {
        manager.notify(id, notification)
    } catch (_: SecurityException) {
    }
}
