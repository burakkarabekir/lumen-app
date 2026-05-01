@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bksd.journal.presentation.journal

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.JournalFilter
import com.bksd.journal.domain.model.applyFilter
import com.bksd.journal.domain.usecase.GetMomentsByDateUseCase
import com.bksd.journal.domain.usecase.SyncMomentsUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class JournalViewModel(
    private val getMomentsByDateUseCase: GetMomentsByDateUseCase,
    private val syncMomentsUseCase: SyncMomentsUseCase,
    private val clock: Clock,
    private val timeZone: TimeZone
) : BaseViewModel<JournalAction, JournalEvent>() {

    private var hasLoadedInitialData = false

    private val selectedDate = MutableStateFlow(
        clock.todayIn(timeZone)
    )
    private val selectedFilter = MutableStateFlow(JournalFilter.ALL)
    private val isSyncing = MutableStateFlow(false)
    private val error = MutableStateFlow<UiText?>(null)

    val state: StateFlow<JournalState> = combine(
        selectedDate.flatMapLatest { date -> getMomentsByDateUseCase(date) },
        selectedFilter,
        selectedDate,
        isSyncing,
        error
    ) { moments, filter, date, syncing, err ->
        JournalState(
            moments = moments.applyFilter(filter).toPersistentList(),
            isLoading = false,
            isSyncing = syncing,
            error = err,
            selectedFilter = filter,
            selectedDate = date
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                syncFromRemote(selectedDate.value)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = JournalState(
                isLoading = true,
                selectedDate = clock.todayIn(timeZone)
            )
        )

    override fun onAction(action: JournalAction) {
        when (action) {
            JournalAction.OnCreateNewClick -> sendEvent(JournalEvent.NavigateToCreate)
            is JournalAction.OnFilterSelect -> selectedFilter.update { action.filter }
            is JournalAction.OnMomentClick -> sendEvent(JournalEvent.NavigateToDetail(action.id))
            is JournalAction.OnDateSelect -> {
                selectedDate.update { action.date }
                syncFromRemote(action.date)
            }
        }
    }

    private fun syncFromRemote(date: LocalDate) {
        launch {
            isSyncing.update { true }
            error.update { null }
            when (val result = syncMomentsUseCase(date)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    error.update { errorText }
                    sendEvent(JournalEvent.ShowError(errorText))
                }

                is Result.Success -> { /* Room Flow auto-updates the UI */
                }
            }
            isSyncing.update { false }
        }
    }
}
