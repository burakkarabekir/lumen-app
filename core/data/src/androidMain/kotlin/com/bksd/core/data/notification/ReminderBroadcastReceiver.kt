package com.bksd.core.data.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bksd.core.domain.notification.ReminderScheduler
import com.bksd.core.domain.reminder.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Reminder"
        val body = intent.getStringExtra(EXTRA_BODY).orEmpty()
        postReminderNotification(context, id, title, body)

        val koin = GlobalContext.getOrNull() ?: return
        val repository = koin.get<ReminderRepository>()
        val scheduler = koin.get<ReminderScheduler>()
        val pending = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                scheduler.reschedule(repository.observe().first())
            } finally {
                pending.finish()
            }
        }
    }
}
