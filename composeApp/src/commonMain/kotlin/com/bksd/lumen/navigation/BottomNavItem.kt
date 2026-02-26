package com.bksd.lumen.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.bksd.lumen.navigation.route.Route

data class BottomNavItem(
    val icon: ImageVector,
    val title: String,
)

val TOP_LEVEL_DESTINATIONS: Map<NavKey, BottomNavItem> = mapOf(
    Route.Main.Journal to BottomNavItem(
        icon = Icons.Outlined.Book,
        title = "Journal"
    ),
    Route.Main.Insights to BottomNavItem(
        icon = Icons.Outlined.BarChart,
        title = "Insights"
    ),
    Route.Main.Profile to BottomNavItem(
        icon = Icons.Outlined.PersonOutline,
        title = "Profile"
    ),
)