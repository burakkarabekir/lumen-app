package com.bksd.journal.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.usecase.GetMomentUseCase
import com.bksd.journal.presentation.journal.JournalState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class MomentDetailViewModel(
    private val getMomentUseCase: GetMomentUseCase,
    private val momentId: String
) : BaseViewModel<MomentDetailAction, MomentDetailEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MomentDetailState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadMoment(momentId)
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
            is MomentDetailAction.OnNavigateBack -> sendEvent(MomentDetailEvent.NavigateBack)
            is MomentDetailAction.OnEditClick -> {
                state.value.moment?.let {
                    sendEvent(MomentDetailEvent.NavigateToEdit(it.id))
                }
            }
        }
    }

    private fun loadMoment(id: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        launch {
            when (val result = getMomentUseCase(id)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    _state.update { it.copy(isLoading = false, error = errorText) }
                    sendEvent(MomentDetailEvent.ShowError(errorText))
                }

                is Result.Success -> {
                    _state.update { it.copy(  isLoading = false,
                        moment = result.data) }
                }
            }
        }
    }
}
