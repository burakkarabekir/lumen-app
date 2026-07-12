package com.bksd.core.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val REMINDER_CHANNEL_ID = "lumen_reminders_v2"
const val EXTRA_OPEN_CREATE_MOMENT = "open_create_moment"

internal const val EXTRA_ID = "reminder_id"
internal const val EXTRA_TITLE = "reminder_title"
internal const val EXTRA_BODY = "reminder_body"

fun createReminderChannel(context: Context) {
    val manager = context.getSystemService(NotificationManager::class.java)
    manager.deleteNotificationChannel("lumen_reminders")
    val channel = NotificationChannel(
        REMINDER_CHANNEL_ID,
        "Reminders",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Journaling reminders"
        enableVibration(true)
    }
    manager.createNotificationChannel(channel)
}

internal fun postReminderNotification(context: Context, id: Int, title: String, body: String) {
    createReminderChannel(context)
    val manager = NotificationManagerCompat.from(context)
    if (!manager.areNotificationsEnabled()) return
    val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_popup_reminder)
        .setContentTitle(title)
        .setContentText(body)
        .setAutoCancel(true)
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    reminderContentIntent(context, id)?.let { builder.setContentIntent(it) }
    try {
        manager.notify(id, builder.build())
    } catch (_: SecurityException) {
    }
}

private fun reminderContentIntent(context: Context, id: Int): PendingIntent? {
    val launchIntent = context.packageManager
        .getLaunchIntentForPackage(context.packageName)
        ?.apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(EXTRA_OPEN_CREATE_MOMENT, true)
        }
        ?: return null
    return PendingIntent.getActivity(
        context,
        id,
        launchIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
