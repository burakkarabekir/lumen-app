package com.bksd.insights.presentation.reflection

import androidx.compose.ui.graphics.Color

fun reflectionHexColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    val value = clean.toLongOrNull(16) ?: return Color(0xFF7682D6)
    return if (clean.length <= 6) Color(0xFF000000 or value) else Color(value)
}
