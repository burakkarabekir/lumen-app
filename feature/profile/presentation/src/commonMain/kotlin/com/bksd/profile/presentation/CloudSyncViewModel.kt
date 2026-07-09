package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.toUiText
import com.bksd.insights.domain.usecase.ObserveAllMomentsUseCase
import com.bksd.insights.domain.usecase.SyncAllMomentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CloudSyncViewModel(
    private val observeAllMoments: ObserveAllMomentsUseCase,
    private val syncAllMoments: SyncAllMomentsUseCase,
    private val authRepository: AuthRepository,
) : BaseViewModel<CloudSyncAction, CloudSyncEvent>() {

    private var hasLoaded = false

    private val _state = MutableStateFlow(CloudSyncState())
    val state = _state
        .onStart {
            if (!hasLoaded) {
                hasLoaded = true
                _state.update { it.copy(accountName = authRepository.getDisplayName().orEmpty()) }
                observeCount()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CloudSyncState(),
        )

    private fun observeCount() {
        launch {
            observeAllMoments().collect { moments ->
                _state.update { it.copy(entriesCount = moments.size) }
            }
        }
    }

    override fun onAction(action: CloudSyncAction) {
        when (action) {
            CloudSyncAction.OnSyncNowClick -> handleSync()
        }
    }

    private fun handleSync() {
        if (_state.value.isSyncing) return
        _state.update { it.copy(isSyncing = true) }
        launch {
            val result = syncAllMoments()
            _state.update { it.copy(isSyncing = false) }
            when (result) {
                is Result.Success -> sendEvent(CloudSyncEvent.SyncSuccess)
                is Result.Error -> sendEvent(CloudSyncEvent.SyncError(result.error.toUiText()))
            }
        }
    }
}
