package com.bksd.journal.presentation.journal

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.usecase.GetMomentsUseCase
import kotlinx.coroutines.launch

class JournalViewModel(
    private val getMomentsUseCase: GetMomentsUseCase
) : BaseViewModel<JournalAction, JournalEvent>() {

    var _state = JournalState()
        private set

    init {
        onAction(JournalAction.LoadMoments)
    }

    override fun onAction(action: JournalAction) {
        when (action) {
            is JournalAction.LoadMoments -> loadMoments()
            is JournalAction.OnCreateNewClick -> {
                sendEvent(JournalEvent.NavigateToCreate)
            }

            is JournalAction.OnFilterSelect -> {
                _state = _state.copy(selectedFilter = action.filter)
                // Filter logic would be applied to the moments list here
            }

            is JournalAction.OnMomentClick -> {
                sendEvent(JournalEvent.NavigateToDetail(action.id))
            }
        }
    }

    private fun loadMoments() {
        _state = _state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val result = getMomentsUseCase()) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    _state = _state.copy(isLoading = false, error = errorText)
                    sendEvent(JournalEvent.ShowError(errorText))
                }

                is Result.Success -> {
                    _state = _state.copy(
                        isLoading = false,
                        moments = result.data
                    )
                }
            }
        }
    }
}
