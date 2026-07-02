package com.bksd.core.data.reminder

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.core.domain.reminder.ReminderRepository
import com.bksd.core.domain.reminder.ReminderSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ReminderRepository {

    private companion object {
        val DAILY_ENABLED = booleanPreferencesKey("reminder_daily_enabled")
        val HOUR = intPreferencesKey("reminder_hour")
        val MINUTE = intPreferencesKey("reminder_minute")
        val DAYS = stringPreferencesKey("reminder_days")
        val STREAK_ENABLED = booleanPreferencesKey("reminder_streak_enabled")
    }

    override fun observe(): Flow<ReminderSettings> = dataStore.data.map { prefs ->
        ReminderSettings(
            dailyEnabled = prefs[DAILY_ENABLED] ?: false,
            hour = prefs[HOUR] ?: 20,
            minute = prefs[MINUTE] ?: 0,
            days = prefs[DAYS]?.let { csv ->
                if (csv.isBlank()) emptySet()
                else csv.split(",").mapNotNull { it.toIntOrNull() }.toSet()
            } ?: ReminderSettings.ALL_DAYS,
            streakEnabled = prefs[STREAK_ENABLED] ?: false,
        )
    }

    override suspend fun save(settings: ReminderSettings) {
        dataStore.edit { prefs ->
            prefs[DAILY_ENABLED] = settings.dailyEnabled
            prefs[HOUR] = settings.hour
            prefs[MINUTE] = settings.minute
            prefs[DAYS] = settings.days.sorted().joinToString(",")
            prefs[STREAK_ENABLED] = settings.streakEnabled
        }
    }
}
