package com.bksd.profile.presentation

sealed interface ExportJournalAction {
    data object OnExportClick : ExportJournalAction
}
