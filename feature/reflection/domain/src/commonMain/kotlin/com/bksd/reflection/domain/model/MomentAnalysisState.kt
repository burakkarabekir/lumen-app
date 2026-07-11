package com.bksd.reflection.domain.model

sealed interface MomentAnalysisState {
    data object None : MomentAnalysisState
    data object Pending : MomentAnalysisState
    data class Ready(val reflection: MomentReflection) : MomentAnalysisState
    data object Failed : MomentAnalysisState
    data object Offline : MomentAnalysisState
    data object QuotaExceeded : MomentAnalysisState
}
