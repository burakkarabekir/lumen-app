package com.bksd.insights.presentation.reflection

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.billing.ObserveEntitlementUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.insights.domain.usecase.ObserveAllMomentsUseCase
import com.bksd.reflection.domain.usecase.GenerateWeeklyReflectionUseCase
import com.bksd.reflection.domain.usecase.ObserveWeeklyReflectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ReflectionViewModel(
    private val observeWeeklyReflection: ObserveWeeklyReflectionUseCase,
    private val generateWeeklyReflection: GenerateWeeklyReflectionUseCase,
    private val observeAllMoments: ObserveAllMomentsUseCase,
    private val observeEntitlement: ObserveEntitlementUseCase,
) : BaseViewModel<ReflectionAction, ReflectionEvent>() {

    private var started = false

    private val _state = MutableStateFlow(ReflectionState())
    val state = _state
        .onStart {
            if (!started) {
                started = true
                observeCache()
                observePremiumAndGenerate()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), ReflectionState())

    override fun onAction(action: ReflectionAction) {
        when (action) {
            ReflectionAction.OnViewFull -> sendEvent(ReflectionEvent.NavigateToFullReflection)
            ReflectionAction.OnUnlock -> sendEvent(ReflectionEvent.NavigateToPaywall)
            ReflectionAction.OnRetry -> generate()
        }
    }

    private fun observeCache() {
        launch {
            observeWeeklyReflection().collect { reflection ->
                _state.update { it.copy(reflection = reflection) }
            }
        }
    }

    private fun observePremiumAndGenerate() {
        launch {
            observeEntitlement().collect { entitlement ->
                _state.update { it.copy(isPremium = entitlement.isPlus) }
                if (entitlement.isPlus) generateIfStale()
            }
        }
    }

    private fun generateIfStale() {
        launch {
            val cached = observeWeeklyReflection().first()
            val newestEntryMs = observeAllMoments().first()
                .maxOfOrNull { it.createdAt.toEpochMilliseconds() } ?: return@launch
            if (cached == null || cached.generatedAtMs < newestEntryMs) generate()
        }
    }

    private fun generate() {
        if (_state.value.isGenerating) return
        _state.update { it.copy(isGenerating = true, error = false) }
        launch {
            val failed = generateWeeklyReflection() is Result.Error
            _state.update { it.copy(isGenerating = false, error = failed) }
        }
    }
}
