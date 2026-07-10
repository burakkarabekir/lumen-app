package com.bksd.profile.presentation

sealed interface LegalDocumentAction {
    data object OnPageStarted : LegalDocumentAction
    data object OnPageFinished : LegalDocumentAction
    data object OnError : LegalDocumentAction
    data object OnRetry : LegalDocumentAction
}
