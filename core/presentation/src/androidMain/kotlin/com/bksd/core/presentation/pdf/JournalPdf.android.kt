package com.bksd.core.presentation.pdf

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842
private const val MARGIN = 48f

actual fun generateJournalPdf(content: JournalPdfContent): ByteArray {
    val document = PdfDocument()
    val contentWidth = PAGE_WIDTH - 2 * MARGIN

    val docTitlePaint = paint("#1A1A22", 24f, bold = true)
    val subtitlePaint = paint("#6B6B78", 12f)
    val datePaint = paint("#8A8A94", 11f)
    val titlePaint = paint("#1A1A22", 16f, bold = true)
    val bodyPaint = paint("#33333B", 12f)
    val metaPaint = paint("#8A8A94", 11f)

    var pageNumber = 1
    var page = document.startPage(
        PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
    )
    var canvas = page.canvas
    var y = MARGIN

    fun lineHeight(p: Paint): Float {
        val fm = p.fontMetrics
        return (fm.descent - fm.ascent) * 1.15f
    }

    fun startNewPage() {
        document.finishPage(page)
        pageNumber += 1
        page = document.startPage(
            PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        )
        canvas = page.canvas
        y = MARGIN
    }

    fun wrap(text: String, p: Paint): List<String> {
        val lines = mutableListOf<String>()
        text.split("\n").forEach { paragraph ->
            if (paragraph.isEmpty()) {
                lines.add("")
                return@forEach
            }
            var remaining = paragraph
            while (remaining.isNotEmpty()) {
                var count = p.breakText(remaining, true, contentWidth, null)
                if (count <= 0) count = 1
                if (count < remaining.length) {
                    val lastSpace = remaining.lastIndexOf(' ', count - 1)
                    if (lastSpace > 0) count = lastSpace + 1
                }
                lines.add(remaining.substring(0, count).trimEnd())
                remaining = remaining.substring(count)
            }
        }
        return lines
    }

    fun drawParagraph(text: String, p: Paint) {
        if (text.isBlank()) return
        val lh = lineHeight(p)
        wrap(text, p).forEach { line ->
            if (y + lh > PAGE_HEIGHT - MARGIN) startNewPage()
            y += lh
            canvas.drawText(line, MARGIN, y, p)
        }
    }

    y += lineHeight(docTitlePaint)
    canvas.drawText(content.documentTitle, MARGIN, y, docTitlePaint)
    drawParagraph(content.subtitle, subtitlePaint)
    y += MARGIN / 2f

    content.entries.forEach { entry ->
        if (y + 90f > PAGE_HEIGHT - MARGIN) startNewPage()
        drawParagraph(entry.date, datePaint)
        drawParagraph(entry.title, titlePaint)
        drawParagraph(entry.body, bodyPaint)
        if (entry.meta.isNotBlank()) drawParagraph(entry.meta, metaPaint)
        y += 18f
    }

    document.finishPage(page)
    val out = ByteArrayOutputStream()
    document.writeTo(out)
    document.close()
    return out.toByteArray()
}

private fun paint(hex: String, size: Float, bold: Boolean = false): Paint = Paint().apply {
    isAntiAlias = true
    color = Color.parseColor(hex)
    textSize = size
    if (bold) typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
}
