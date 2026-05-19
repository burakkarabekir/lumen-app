package com.bksd.journal.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.usecase.DeleteMomentUseCase
import com.bksd.journal.domain.usecase.GetMomentUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MomentDetailViewModel(
    private val getMomentUseCase: GetMomentUseCase,
    private val deleteMomentUseCase: DeleteMomentUseCase,
    private val momentId: String
) : BaseViewModel<MomentDetailAction, MomentDetailEvent>() {

    private var hasLoadedInitialData = false

    private val isLoading = MutableStateFlow(true)
    private val error = MutableStateFlow<UiText?>(null)
    private val momentState = MutableStateFlow<com.bksd.journal.domain.model.Moment?>(null)
    private val isEditing = MutableStateFlow(false)
    private val isBodyExpanded = MutableStateFlow(false)
    private val editTitle = MutableStateFlow("")
    private val editBody = MutableStateFlow("")
    private val editMoods = MutableStateFlow(emptySet<com.bksd.journal.domain.model.Mood>())
    private val editTags = MutableStateFlow(emptyList<String>())
    private val isSaving = MutableStateFlow(false)

    @Suppress("UNCHECKED_CAST")
    val state: StateFlow<MomentDetailState> = combine(
        listOf(
            isLoading,
            momentState,
            error,
            isEditing,
            isBodyExpanded,
            editTitle,
            editBody,
            editMoods,
            editTags,
            isSaving
        )
    ) { values ->
        MomentDetailState(
            isLoading = values[0] as Boolean,
            moment = values[1] as? com.bksd.journal.domain.model.Moment,
            error = values[2] as? UiText,
            isEditing = values[3] as Boolean,
            isBodyExpanded = values[4] as Boolean,
            editTitle = values[5] as String,
            editBody = values[6] as String,
            editMoods = (values[7] as Set<com.bksd.journal.domain.model.Mood>).toPersistentSet(),
            editTags = (values[8] as List<String>).toPersistentList(),
            isSaving = values[9] as Boolean
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadMoment()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MomentDetailState()
        )

    override fun onAction(action: MomentDetailAction) {
        when (action) {
            MomentDetailAction.OnNavigateBack -> handleNavigateBack()
            MomentDetailAction.OnMenuClick -> Unit
            MomentDetailAction.OnEditClick -> enterEditMode()
            MomentDetailAction.OnSaveChanges -> handleSaveChanges()
            MomentDetailAction.OnCancelEdit -> exitEditMode()
            MomentDetailAction.OnDeleteClick -> handleDelete()
            MomentDetailAction.OnShareClick -> Unit
            MomentDetailAction.OnFavoriteToggle -> Unit
            MomentDetailAction.OnToggleBodyExpand -> isBodyExpanded.update { !it }
            is MomentDetailAction.OnTitleChange -> editTitle.update { action.title }
            is MomentDetailAction.OnBodyChange -> editBody.update { action.body }
            is MomentDetailAction.OnMoodToggle -> toggleMood(action.mood)
            is MomentDetailAction.OnTagRemove -> editTags.update { it - action.tag }
        }
    }

    private fun handleNavigateBack() {
        if (isEditing.value) {
            exitEditMode()
        } else {
            sendEvent(MomentDetailEvent.NavigateBack)
        }
    }

    private fun enterEditMode() {
        val moment = momentState.value ?: return
        editTitle.update { moment.title }
        editBody.update { moment.body.orEmpty() }
        editMoods.update { moment.moods.toSet() }
        editTags.update { moment.tags }
        isEditing.update { true }
    }

    private fun exitEditMode() {
        isEditing.update { false }
    }

    private fun handleSaveChanges() {
        isSaving.update { true }
        launch {
            val moment = momentState.value ?: return@launch
            val updated = moment.copy(
                title = editTitle.value,
                body = editBody.value.ifEmpty { null },
                moods = editMoods.value.toList(),
                tags = editTags.value
            )
            momentState.update { updated }
            isEditing.update { false }
            isSaving.update { false }
            sendEvent(MomentDetailEvent.ShowSuccess(UiText.Dynamic("Changes saved")))
        }
    }

    private fun toggleMood(mood: com.bksd.journal.domain.model.Mood) {
        editMoods.update { current ->
            if (mood in current) current - mood else current + mood
        }
    }

    private fun loadMoment() {
        isLoading.update { true }
        error.update { null }
        launch {
            when (val result = getMomentUseCase(momentId)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    error.update { errorText }
                    isLoading.update { false }
                    sendEvent(MomentDetailEvent.ShowError(errorText))
                }

                is Result.Success -> {
                    momentState.update { result.data }
                    isLoading.update { false }
                }
            }
        }
    }

    private fun handleDelete() {
        launch {
            when (val result = deleteMomentUseCase(momentId)) {
                is Result.Success -> {
                    sendEvent(MomentDetailEvent.ShowSuccess(UiText.Dynamic("Moment deleted")))
                    sendEvent(MomentDetailEvent.NavigateBack)
                }

                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    sendEvent(MomentDetailEvent.ShowError(errorText))
                }
            }
        }
    }
}
