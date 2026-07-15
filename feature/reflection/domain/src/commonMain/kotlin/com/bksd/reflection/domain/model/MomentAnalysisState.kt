package com.bksd.reflection.domain.model

sealed interface MomentAnalysisState {
    data object None : MomentAnalysisState
    data object Pending : MomentAnalysisState
    data class Ready(val reflection: MomentReflection) : MomentAnalysisState
    data object Failed : MomentAnalysisState
    data object Offline : MomentAnalysisState
    data class QuotaExceeded(val limit: QuotaLimit) : MomentAnalysisState
}

enum class QuotaLimit { DAILY, FREE }
