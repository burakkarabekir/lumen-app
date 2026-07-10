package com.bksd.profile.presentation

data class LegalDocumentState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val reloadTrigger: Int = 0,
)
