package com.bksd.journal.presentation.detail

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.usecase.GetMomentUseCase
import kotlinx.coroutines.launch

class MomentDetailViewModel(
    private val getMomentUseCase: GetMomentUseCase,
    private val momentId: String
) : BaseViewModel<MomentDetailAction, MomentDetailEvent>() {

    var _state = MomentDetailState()
        private set

    init {
        onAction(MomentDetailAction.LoadMoment(momentId))
    }

    override fun onAction(action: MomentDetailAction) {
        when (action) {
            is MomentDetailAction.LoadMoment -> loadMoment(action.id)
            is MomentDetailAction.OnNavigateBack -> {
                sendEvent(MomentDetailEvent.NavigateBack)
            }

            is MomentDetailAction.OnEditClick -> {
                _state.moment?.let {
                    sendEvent(MomentDetailEvent.NavigateToEdit(it.id))
                }
            }
        }
    }

    private fun loadMoment(id: String) {
        _state = _state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val result = getMomentUseCase(id)) {
                is Result.Error -> {
                    val errorText = UiText.Dynamic(result.error.toString())
                    _state = _state.copy(isLoading = false, error = errorText)
                    sendEvent(MomentDetailEvent.ShowError(errorText))
                }

                is Result.Success -> {
                    _state = _state.copy(
                        isLoading = false,
                        moment = result.data
                    )
                }
            }
        }
    }
}
