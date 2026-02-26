package com.bksd.journal.presentation.detail

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText
import com.bksd.journal.domain.model.Moment

@Immutable
data class MomentDetailState(
    val isLoading: Boolean = false,
    val moment: Moment? = null,
    val error: UiText? = null
)
