package com.bksd.lumen.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.bksd.core.design_system.theme.LumenTheme
import com.bksd.core.design_system.theme.labelXSmall
import com.bksd.lumen.navigation.route.Route

@Composable
fun AppNavigationBar(
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .padding(horizontal = 64.dp, vertical = 8.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 8.dp,
        tonalElevation = 2.dp,
    ) {
        CompositionLocalProvider(LocalRippleConfiguration provides null) {
            Row {
                TOP_LEVEL_DESTINATIONS.forEach { (topLevelDestination, data) ->
                    val isSelected = topLevelDestination == selectedKey
                    NavigationBarItem(
                        onClick = { onSelectKey(topLevelDestination) },
                        selected = isSelected,
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .then(
                                        if (isSelected) {
                                            Modifier.background(
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                                shape = CircleShape
                                            )
                                        } else Modifier
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = data.icon,
                                    contentDescription = data.title,
                                    tint = if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    }
                                )
                            }
                        },
                        label = {
                            Text(
                                data.title,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelXSmall
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.5f
                            ),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.5f
                            ),
                            disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    LumenTheme {
        AppNavigationBar(
            selectedKey = Route.Main.Journal,
            onSelectKey = {}
        )
    }
}

@Preview
@Composable
fun PreviewDark() {
    LumenTheme(darkTheme = true) {
        AppNavigationBar(
            selectedKey = Route.Main.Journal,
            onSelectKey = {}
        )
    }
}