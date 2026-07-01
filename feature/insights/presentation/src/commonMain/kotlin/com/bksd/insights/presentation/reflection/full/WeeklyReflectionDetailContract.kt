package com.bksd.insights.presentation.reflection.full

import com.bksd.reflection.domain.model.WeeklyMomentInsights
import com.bksd.reflection.domain.model.WeeklyReflection

data class WeeklyReflectionDetailState(
    val reflection: WeeklyReflection? = null,
    val insights: WeeklyMomentInsights? = null,
    val isLoading: Boolean = true
)

sealed interface WeeklyReflectionDetailAction {
    data object OnBack : WeeklyReflectionDetailAction
    data class OnOpenMoment(val momentId: String) : WeeklyReflectionDetailAction
}

sealed interface WeeklyReflectionDetailEvent {
    data object NavigateBack : WeeklyReflectionDetailEvent
    data class OpenMoment(val momentId: String) : WeeklyReflectionDetailEvent
}
