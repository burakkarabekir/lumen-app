package com.bksd.core.design_system.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Immutable
data class NewEntryPalette(
    val pageBg: Color,
    val surface: Color,
    val text: Color,
    val bodyText: Color,
    val sub: Color,
    val hairline: Color,
    val pinBg: Color,
    val pinFg: Color,
    val saveBg: Color,
)

@Composable
fun rememberNewEntryPalette(): NewEntryPalette {
    val extended = MaterialTheme.colorScheme.extended
    return remember(extended) {
        NewEntryPalette(
            pageBg = extended.newEntryPageBg,
            surface = extended.newEntrySurface,
            text = extended.newEntryText,
            bodyText = extended.newEntryBodyText,
            sub = extended.newEntrySub,
            hairline = extended.newEntryHairline,
            pinBg = extended.newEntryPinBg,
            pinFg = extended.newEntryPinFg,
            saveBg = extended.newEntrySaveBg,
        )
    }
}
