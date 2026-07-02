package com.bksd.core.design_system.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

enum class ShareStyle { AURORA, PAPER, INK, PHOTO }

@Immutable
data class ShareCardColors(
    val backgroundTop: Color,
    val backgroundBottom: Color,
    val gradient: Boolean,
    val text: Color,
    val sub: Color,
    val logoBg: Color,
    val logoTint: Color,
    val chipBg: Color,
    val divider: Color,
    val onImageScrim: Boolean,
)

fun shareCardColors(style: ShareStyle): ShareCardColors = when (style) {
    ShareStyle.AURORA -> ShareCardColors(
        backgroundTop = ShareAuroraTop,
        backgroundBottom = ShareAuroraBottom,
        gradient = true,
        text = Color.White,
        sub = Color.White.copy(alpha = 0.78f),
        logoBg = ShareAuroraLogoBg,
        logoTint = Color.White,
        chipBg = ShareAuroraChipBg,
        divider = Color.White.copy(alpha = 0.25f),
        onImageScrim = false,
    )
    ShareStyle.PAPER -> ShareCardColors(
        backgroundTop = SharePaperBg,
        backgroundBottom = SharePaperBg,
        gradient = false,
        text = SharePaperText,
        sub = SharePaperSub,
        logoBg = SharePaperChipBg,
        logoTint = SharePaperText,
        chipBg = SharePaperChipBg,
        divider = Color.Black.copy(alpha = 0.08f),
        onImageScrim = false,
    )
    ShareStyle.INK -> ShareCardColors(
        backgroundTop = ShareInkBgTop,
        backgroundBottom = ShareInkBgBottom,
        gradient = true,
        text = ShareInkText,
        sub = ShareInkSub,
        logoBg = ShareInkChipBg,
        logoTint = ShareInkText,
        chipBg = ShareInkChipBg,
        divider = Color.White.copy(alpha = 0.10f),
        onImageScrim = false,
    )
    ShareStyle.PHOTO -> ShareCardColors(
        backgroundTop = SharePhotoTop,
        backgroundBottom = SharePhotoBottom,
        gradient = true,
        text = Color.White,
        sub = Color.White.copy(alpha = 0.82f),
        logoBg = Color.White.copy(alpha = 0.2f),
        logoTint = Color.White,
        chipBg = Color.White.copy(alpha = 0.22f),
        divider = Color.White.copy(alpha = 0.28f),
        onImageScrim = true,
    )
}
