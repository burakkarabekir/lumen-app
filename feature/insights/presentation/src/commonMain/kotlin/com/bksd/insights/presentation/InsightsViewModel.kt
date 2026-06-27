package com.bksd.insights.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.toUiText
import com.bksd.insights.domain.model.InsightsRange
import com.bksd.insights.domain.usecase.ComputeInsightsUseCase
import com.bksd.insights.domain.usecase.ObserveAllMomentsUseCase
import com.bksd.insights.domain.usecase.SyncAllMomentsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class InsightsViewModel(
    observeAllMoments: ObserveAllMomentsUseCase,
    private val syncAllMoments: SyncAllMomentsUseCase,
    private val computeInsights: ComputeInsightsUseCase,
) : BaseViewModel<InsightsAction, InsightsEvent>() {

    private val selectedRange = MutableStateFlow<InsightsRange>(InsightsRange.AllTime)

    val state: StateFlow<InsightsState> = combine(
        observeAllMoments(),
        selectedRange
    ) { moments, range ->
        computeInsights(moments, range).toUiState(range)
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = InsightsState(isLoading = true)
        )

    init {
        sync()
    }

    override fun onAction(action: InsightsAction) {
        when (action) {
            InsightsAction.OnRefresh -> sync()
            is InsightsAction.OnStatsRangeSelect ->
                selectedRange.value = action.range.toDomain()
        }
    }

    private fun sync() {
        launch {
            val result = syncAllMoments()
            if (result is Result.Error) {
                sendEvent(InsightsEvent.ShowError(result.error.toUiText()))
            }
        }
    }
}
