package com.bksd.insights.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.bksd.core.design_system.theme.ExtendedColors
import com.bksd.core.design_system.theme.profileAccentAmber
import com.bksd.core.design_system.theme.profileAccentGreen
import com.bksd.core.design_system.theme.profileAccentIndigo
import com.bksd.core.design_system.theme.profileAccentRed
import com.bksd.core.design_system.theme.profileAccentViolet

internal fun placeKindIcon(kind: PlaceKind): ImageVector = when (kind) {
    PlaceKind.BEACH -> Icons.Default.BeachAccess
    PlaceKind.LANDMARK -> Icons.Default.AccountBalance
    PlaceKind.PARK -> Icons.Default.Park
    PlaceKind.RESTAURANT -> Icons.Default.Restaurant
    PlaceKind.GENERIC -> Icons.Default.Place
}

internal fun placeAccentColor(index: Int, extended: ExtendedColors): Color =
    when (((index % 5) + 5) % 5) {
        0 -> extended.profileAccentIndigo
        1 -> extended.profileAccentRed
        2 -> extended.profileAccentGreen
        3 -> extended.profileAccentViolet
        else -> extended.profileAccentAmber
    }
