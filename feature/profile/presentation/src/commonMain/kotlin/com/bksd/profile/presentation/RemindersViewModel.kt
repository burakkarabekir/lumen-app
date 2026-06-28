package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.notification.ReminderScheduler
import com.bksd.core.domain.reminder.ReminderRepository
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class RemindersViewModel(
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler,
) : BaseViewModel<RemindersAction, RemindersEvent>() {

    private var hasLoaded = false

    private val _state = MutableStateFlow(RemindersState())
    val state = _state
        .onStart {
            if (!hasLoaded) {
                hasLoaded = true
                launch {
                    val settings = reminderRepository.observe().first()
                    _state.update {
                        it.copy(
                            dailyEnabled = settings.dailyEnabled,
                            hour = settings.hour,
                            minute = settings.minute,
                            days = settings.days,
                            streakEnabled = settings.streakEnabled,
                            loaded = true
                        )
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RemindersState()
        )

    override fun onAction(action: RemindersAction) {
        if (!_state.value.loaded) return
        val updated = when (action) {
            is RemindersAction.OnDailyToggle -> _state.value.copy(dailyEnabled = action.enabled)
            is RemindersAction.OnTimeSelected -> _state.value.copy(
                hour = action.hour,
                minute = action.minute
            )

            is RemindersAction.OnStreakToggle -> _state.value.copy(streakEnabled = action.enabled)
            is RemindersAction.OnDayToggle -> {
                val days = _state.value.days.toMutableSet().apply {
                    if (!add(action.day) && size > 1) remove(action.day)
                }
                _state.value.copy(days = days)
            }
        }
        _state.value = updated
        persist(updated)
    }

    private fun persist(state: RemindersState) {
        launch {
            val settings = state.toSettings()
            reminderRepository.save(settings)
            reminderScheduler.reschedule(settings)
        }
    }
}
