package com.bksd.insights.presentation.reflection

import com.bksd.reflection.domain.model.WeeklyReflection

data class ReflectionState(
    val reflection: WeeklyReflection? = null,
    val isGenerating: Boolean = false,
    val error: Boolean = false
)

sealed interface ReflectionAction {
    data object OnViewFull : ReflectionAction
}

sealed interface ReflectionEvent {
    data object NavigateToFullReflection : ReflectionEvent
}
