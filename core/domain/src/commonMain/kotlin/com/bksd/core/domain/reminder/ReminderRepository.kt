package com.bksd.core.domain.reminder

import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun observe(): Flow<ReminderSettings>
    suspend fun save(settings: ReminderSettings)
}
