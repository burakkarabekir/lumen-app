package com.bksd.core.presentation.pdf

data class JournalPdfEntry(
    val date: String,
    val title: String,
    val body: String,
    val meta: String,
)

data class JournalPdfContent(
    val documentTitle: String,
    val subtitle: String,
    val entries: List<JournalPdfEntry>,
)

/**
 * Renders [content] into a single PDF document and returns the raw bytes.
 * Android uses android.graphics.pdf.PdfDocument; iOS uses UIGraphicsPDFRenderer.
 */
expect fun generateJournalPdf(content: JournalPdfContent): ByteArray
