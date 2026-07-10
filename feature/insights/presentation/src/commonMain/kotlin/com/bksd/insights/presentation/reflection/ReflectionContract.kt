package com.bksd.insights.presentation.reflection

import androidx.compose.runtime.Immutable
import com.bksd.reflection.domain.model.WeeklyReflection

@Immutable
data class ReflectionState(
    val reflection: WeeklyReflection? = null,
    val isGenerating: Boolean = false,
    val error: Boolean = false,
    val isPremium: Boolean = false,
)

sealed interface ReflectionAction {
    data object OnViewFull : ReflectionAction
    data object OnUnlock : ReflectionAction
    data object OnRetry : ReflectionAction
}

sealed interface ReflectionEvent {
    data object NavigateToFullReflection : ReflectionEvent
    data object NavigateToPaywall : ReflectionEvent
}
