package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.model.Moment
import com.bksd.core.presentation.labelRes
import com.bksd.core.presentation.pdf.JournalPdfContent
import com.bksd.core.presentation.pdf.JournalPdfEntry
import com.bksd.core.presentation.pdf.generateJournalPdf
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.insights.domain.usecase.ObserveAllMomentsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString

class ExportJournalViewModel(
    private val observeAllMoments: ObserveAllMomentsUseCase,
) : BaseViewModel<ExportJournalAction, ExportJournalEvent>() {

    private var hasLoaded = false

    private val _state = MutableStateFlow(ExportJournalState())
    val state = _state
        .onStart {
            if (!hasLoaded) {
                hasLoaded = true
                observeCount()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ExportJournalState(),
        )

    private fun observeCount() {
        launch {
            observeAllMoments().collect { moments ->
                _state.update { it.copy(entriesCount = moments.size) }
            }
        }
    }

    override fun onAction(action: ExportJournalAction) {
        when (action) {
            ExportJournalAction.OnExportClick -> handleExport()
        }
    }

    private fun handleExport() {
        if (_state.value.isExporting) return
        _state.update { it.copy(isExporting = true) }
        launch {
            val moments = observeAllMoments().first()
            if (moments.isEmpty()) {
                _state.update { it.copy(isExporting = false) }
                sendEvent(ExportJournalEvent.Empty)
                return@launch
            }
            val content = buildContent(moments)
            val bytes = try {
                withContext(Dispatchers.Default) { generateJournalPdf(content) }
            } catch (e: Exception) {
                _state.update { it.copy(isExporting = false) }
                sendEvent(ExportJournalEvent.Error(e.message ?: "unknown"))
                return@launch
            }
            _state.update { it.copy(isExporting = false) }
            sendEvent(ExportJournalEvent.SharePdf(bytes, "Lumen-Journal.pdf"))
        }
    }

    private suspend fun buildContent(moments: List<Moment>): JournalPdfContent {
        val entries = moments
            .sortedByDescending { it.createdAt }
            .map { moment ->
                val moodStr = moment.moods.map { "${it.emoji} ${getString(it.labelRes())}" }.joinToString("   ")
                val tagStr = moment.tags.joinToString(" ") { "#$it" }
                val meta = listOf(moodStr, tagStr)
                    .filter { it.isNotBlank() }
                    .joinToString("      ")
                JournalPdfEntry(
                    date = formatDate(moment.createdAt.toString()),
                    title = moment.title,
                    body = moment.body.orEmpty(),
                    meta = meta,
                )
            }
        return JournalPdfContent(
            documentTitle = getString(Res.string.pdf_document_title),
            subtitle = getPluralString(Res.plurals.pdf_entry_count, moments.size, moments.size),
            entries = entries,
        )
    }
}

private suspend fun formatDate(isoInstant: String): String {
    val datePart = isoInstant.substringBefore('T')
    val parts = datePart.split('-')
    if (parts.size != 3) return datePart
    val year = parts[0]
    val month = parts[1].toIntOrNull() ?: return datePart
    val day = parts[2].toIntOrNull() ?: return datePart
    val monthName = monthAbbr(month) ?: return datePart
    return "$day $monthName $year"
}

private suspend fun monthAbbr(month: Int): String? {
    val res = when (month) {
        1 -> Res.string.month_abbr_jan
        2 -> Res.string.month_abbr_feb
        3 -> Res.string.month_abbr_mar
        4 -> Res.string.month_abbr_apr
        5 -> Res.string.month_abbr_may
        6 -> Res.string.month_abbr_jun
        7 -> Res.string.month_abbr_jul
        8 -> Res.string.month_abbr_aug
        9 -> Res.string.month_abbr_sep
        10 -> Res.string.month_abbr_oct
        11 -> Res.string.month_abbr_nov
        12 -> Res.string.month_abbr_dec
        else -> return null
    }
    return getString(res)
}
