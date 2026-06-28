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

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val koin = GlobalContext.getOrNull() ?: return
        val repository = koin.get<ReminderRepository>()
        val scheduler = koin.get<ReminderScheduler>()
        val pending = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val settings = repository.observe().first()
                scheduler.reschedule(settings)
            } finally {
                pending.finish()
            }
        }
    }
}
