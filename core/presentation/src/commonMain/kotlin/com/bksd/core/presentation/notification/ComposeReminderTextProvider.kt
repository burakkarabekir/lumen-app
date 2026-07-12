package com.bksd.core.presentation.notification

import com.bksd.core.domain.notification.ReminderTextProvider
import com.bksd.core.domain.notification.ReminderTexts
import com.bksd.core.presentation.Res
import com.bksd.core.presentation.reminder_daily_body
import com.bksd.core.presentation.reminder_daily_title
import com.bksd.core.presentation.reminder_streak_body
import com.bksd.core.presentation.reminder_streak_title
import org.jetbrains.compose.resources.getString

class ComposeReminderTextProvider : ReminderTextProvider {
    override suspend fun reminderTexts(): ReminderTexts = ReminderTexts(
        dailyTitle = getString(Res.string.reminder_daily_title),
        dailyBody = getString(Res.string.reminder_daily_body),
        streakTitle = getString(Res.string.reminder_streak_title),
        streakBody = getString(Res.string.reminder_streak_body),
    )
}
