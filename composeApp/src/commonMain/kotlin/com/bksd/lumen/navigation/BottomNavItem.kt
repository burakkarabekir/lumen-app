package com.bksd.lumen.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Book
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.bksd.lumen.Res
import com.bksd.lumen.nav_insights
import com.bksd.lumen.nav_journal
import com.bksd.lumen.navigation.route.Route
import org.jetbrains.compose.resources.StringResource

data class BottomNavItem(
    val icon: ImageVector,
    val title: StringResource,
)

val TOP_LEVEL_DESTINATIONS: Map<NavKey, BottomNavItem> = mapOf(
    Route.Main.Journal to BottomNavItem(
        icon = Icons.Outlined.Book,
        title = Res.string.nav_journal
    ),
    Route.Main.Insights to BottomNavItem(
        icon = Icons.Outlined.BarChart,
        title = Res.string.nav_insights
    ),
)
