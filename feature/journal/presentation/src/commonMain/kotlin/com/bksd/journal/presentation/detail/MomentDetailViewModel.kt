package com.bksd.journal.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.usecase.GetMomentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MomentDetailViewModel(
    private val getMomentUseCase: GetMomentUseCase,
    private val momentId: String
) : BaseViewModel<MomentDetailAction, MomentDetailEvent>() {

    private var hasLoadedInitialData = false

    private val isLoading = MutableStateFlow(true)
    private val error = MutableStateFlow<UiText?>(null)
    private val momentState = MutableStateFlow<com.bksd.journal.domain.model.Moment?>(null)

    val state: StateFlow<MomentDetailState> = combine(
        isLoading,
        momentState,
        error
    ) { loading, moment, err ->
        MomentDetailState(
            isLoading = loading,
            moment = moment,
            error = err
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
            is MomentDetailAction.OnNavigateBack -> sendEvent(MomentDetailEvent.NavigateBack)
            is MomentDetailAction.OnEditClick -> {
                momentState.value?.let {
                    sendEvent(MomentDetailEvent.NavigateToEdit(it.id))
                }
            }
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
}
