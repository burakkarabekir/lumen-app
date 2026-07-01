package com.bksd.insights.presentation.reflection.full

import androidx.lifecycle.viewModelScope
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.insights.domain.usecase.ObserveAllMomentsUseCase
import com.bksd.reflection.domain.usecase.BuildWeeklyInsightsUseCase
import com.bksd.reflection.domain.usecase.ObserveWeeklyReflectionUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class WeeklyReflectionDetailViewModel(
    observeWeeklyReflection: ObserveWeeklyReflectionUseCase,
    observeAllMoments: ObserveAllMomentsUseCase,
    private val buildWeeklyInsights: BuildWeeklyInsightsUseCase,
) : BaseViewModel<WeeklyReflectionDetailAction, WeeklyReflectionDetailEvent>() {

    val state = combine(observeWeeklyReflection(), observeAllMoments()) { reflection, moments ->
        WeeklyReflectionDetailState(
            reflection = reflection,
            insights = buildWeeklyInsights(moments),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = WeeklyReflectionDetailState()
    )

    override fun onAction(action: WeeklyReflectionDetailAction) {
        when (action) {
            WeeklyReflectionDetailAction.OnBack ->
                sendEvent(WeeklyReflectionDetailEvent.NavigateBack)

            is WeeklyReflectionDetailAction.OnOpenMoment ->
                sendEvent(WeeklyReflectionDetailEvent.OpenMoment(action.momentId))
        }
    }
}
