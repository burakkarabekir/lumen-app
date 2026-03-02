package com.bksd.journal.presentation.journal

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.LinkAttachment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.usecase.GetMomentsByDateUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class JournalViewModel(
    private val getMomentsByDateUseCase: GetMomentsByDateUseCase
) : BaseViewModel<JournalAction, JournalEvent>() {

    private var hasLoadedInitialData = false
    private var moments: ImmutableList<Moment> = persistentListOf()

    private val _state = MutableStateFlow(JournalState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMomentsByDate(Clock.System.todayIn(TimeZone.currentSystemDefault()))
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = JournalState()
        )

    override fun onAction(action: JournalAction) {
        when (action) {
            is JournalAction.OnCreateNewClick -> {
                sendEvent(JournalEvent.NavigateToCreate)
            }

            is JournalAction.OnFilterSelect -> {
                _state.update { it.copy(selectedFilter = action.filter) }
                applyFilters()
            }

            is JournalAction.OnDateSelect -> {
                loadMomentsByDate(action.date)
            }

            is JournalAction.OnMomentClick -> {
                sendEvent(JournalEvent.NavigateToDetail(action.id))
            }
        }
    }

    private fun loadMomentsByDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date, selectedFilter = FILTER_ALL) }
        launch {
            when (val result = getMomentsByDateUseCase(date)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    _state.update { it.copy(isLoading = false, error = errorText) }
                    sendEvent(JournalEvent.ShowError(errorText))
                }

                is Result.Success -> {
                    moments = result.data.toPersistentList()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            moments = moments.toPersistentList()
                        )
                    }
                    applyFilters()
                }
            }
        }
    }

    private fun applyFilters() {
        val filtered = when (_state.value.selectedFilter) {
            FILTER_PHOTOS -> moments.filter { moment ->
                moment.attachments
                    .any { it is PhotoAttachment }
            }

            FILTER_VIDEOS -> moments.filter { moment ->
                moment.attachments
                    .any { it is VideoAttachment }
            }

            FILTER_VOICE_NOTES -> moments.filter { moment ->
                moment.attachments
                    .any { it is AudioAttachment }
            }

            FILTER_LINKS -> moments.filter { moment ->
                moment.attachments
                    .any { it is LinkAttachment }
            }

            else -> moments // "All Entries"
        }
        _state.update { it.copy(filteredMoments = filtered.toPersistentList()) }
    }

    companion object {
        const val FILTER_ALL = "All Entries"
        const val FILTER_PHOTOS = "Photos"
        const val FILTER_VIDEOS = "Videos"
        const val FILTER_VOICE_NOTES = "Voice Notes"
        const val FILTER_LINKS = "Links"
    }
}
