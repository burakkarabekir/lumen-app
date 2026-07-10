package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LegalDocumentViewModel : BaseViewModel<LegalDocumentAction, LegalDocumentEvent>() {

    private val _state = MutableStateFlow(LegalDocumentState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LegalDocumentState(),
        )

    override fun onAction(action: LegalDocumentAction) {
        when (action) {
            LegalDocumentAction.OnPageStarted -> _state.update { it.copy(isLoading = true) }
            LegalDocumentAction.OnPageFinished -> _state.update { it.copy(isLoading = false) }
            LegalDocumentAction.OnError -> _state.update { it.copy(isLoading = false, isError = true) }
            LegalDocumentAction.OnRetry -> _state.update {
                it.copy(isError = false, isLoading = true, reloadTrigger = it.reloadTrigger + 1)
            }
        }
    }
}
