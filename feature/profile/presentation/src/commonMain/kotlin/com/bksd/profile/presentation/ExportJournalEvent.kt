package com.bksd.profile.presentation

sealed interface ExportJournalEvent {
    class SharePdf(val bytes: ByteArray, val fileName: String) : ExportJournalEvent
    data object Empty : ExportJournalEvent
    data class Error(val message: String) : ExportJournalEvent
}
