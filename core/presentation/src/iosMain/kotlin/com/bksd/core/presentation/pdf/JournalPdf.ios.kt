package com.bksd.core.presentation.pdf

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UIKit.NSFontAttributeName
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsPDFRenderer
import platform.UIKit.UIGraphicsPDFRendererFormat
import platform.UIKit.drawAtPoint
import platform.UIKit.sizeWithAttributes
import platform.posix.memcpy

private const val PAGE_WIDTH = 595.0
private const val PAGE_HEIGHT = 842.0
private const val MARGIN = 48.0

@OptIn(ExperimentalForeignApi::class)
actual fun generateJournalPdf(content: JournalPdfContent): ByteArray {
    val contentWidth = PAGE_WIDTH - 2 * MARGIN

    val docTitleAttrs = attributes(UIFont.boldSystemFontOfSize(24.0), color("#1A1A22"))
    val subtitleAttrs = attributes(UIFont.systemFontOfSize(12.0), color("#6B6B78"))
    val dateAttrs = attributes(UIFont.systemFontOfSize(11.0), color("#8A8A94"))
    val titleAttrs = attributes(UIFont.boldSystemFontOfSize(16.0), color("#1A1A22"))
    val bodyAttrs = attributes(UIFont.systemFontOfSize(12.0), color("#33333B"))
    val metaAttrs = attributes(UIFont.systemFontOfSize(11.0), color("#8A8A94"))

    val renderer = UIGraphicsPDFRenderer(
        bounds = CGRectMake(0.0, 0.0, PAGE_WIDTH, PAGE_HEIGHT),
        format = UIGraphicsPDFRendererFormat(),
    )

    val data = renderer.PDFDataWithActions { ctx ->
        ctx?.beginPage()
        var y = MARGIN

        fun lineHeight(attrs: Map<Any?, *>): Double =
            NSString.create(string = "Ag").sizeWithAttributes(attrs).useContents { height }

        fun wrap(text: String, attrs: Map<Any?, *>): List<String> {
            val lines = mutableListOf<String>()
            text.split("\n").forEach { paragraph ->
                if (paragraph.isEmpty()) {
                    lines.add("")
                    return@forEach
                }
                var current = ""
                paragraph.split(" ").forEach { word ->
                    val candidate = if (current.isEmpty()) word else "$current $word"
                    val candidateWidth = NSString.create(string = candidate)
                        .sizeWithAttributes(attrs).useContents { width }
                    if (candidateWidth > contentWidth && current.isNotEmpty()) {
                        lines.add(current)
                        current = word
                    } else {
                        current = candidate
                    }
                }
                if (current.isNotEmpty()) lines.add(current)
            }
            return lines
        }

        fun draw(text: String, attrs: Map<Any?, *>, spacing: Double) {
            if (text.isBlank()) return
            val lh = lineHeight(attrs)
            wrap(text, attrs).forEach { line ->
                if (y + lh > PAGE_HEIGHT - MARGIN) {
                    ctx?.beginPage()
                    y = MARGIN
                }
                NSString.create(string = line).drawAtPoint(
                    point = CGPointMake(MARGIN, y),
                    withAttributes = attrs,
                )
                y += lh
            }
            y += spacing
        }

        draw(content.documentTitle, docTitleAttrs, 4.0)
        draw(content.subtitle, subtitleAttrs, 16.0)

        content.entries.forEach { entry ->
            if (y + 90.0 > PAGE_HEIGHT - MARGIN) {
                ctx?.beginPage()
                y = MARGIN
            }
            draw(entry.date, dateAttrs, 2.0)
            draw(entry.title, titleAttrs, 4.0)
            draw(entry.body, bodyAttrs, 4.0)
            if (entry.meta.isNotBlank()) draw(entry.meta, metaAttrs, 4.0)
            y += 16.0
        }
    }

    return data.toByteArray()
}

private fun attributes(font: UIFont, color: UIColor): Map<Any?, *> =
    mapOf(
        NSFontAttributeName to font,
        NSForegroundColorAttributeName to color,
    )

private fun color(hex: String): UIColor {
    val clean = hex.removePrefix("#")
    val r = clean.substring(0, 2).toInt(16) / 255.0
    val g = clean.substring(2, 4).toInt(16) / 255.0
    val b = clean.substring(4, 6).toInt(16) / 255.0
    return UIColor(red = r, green = g, blue = b, alpha = 1.0)
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    val result = ByteArray(size)
    result.usePinned { pinned ->
        memcpy(pinned.addressOf(0), bytes, length)
    }
    return result
}
