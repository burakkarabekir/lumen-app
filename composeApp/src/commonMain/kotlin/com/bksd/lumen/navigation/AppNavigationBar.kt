package com.bksd.lumen.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.extended
import com.bksd.lumen.navigation.route.Route
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppNavigationBar(
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = MaterialTheme.colorScheme.extended
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(extended.navCapsule)
                .border(1.dp, extended.navBorder, RoundedCornerShape(25.dp))
                .padding(6.dp)
        ) {
            TOP_LEVEL_DESTINATIONS.forEach { (key, data) ->
                NavTab(
                    icon = data.icon,
                    label = stringResource(data.title),
                    selected = key == selectedKey,
                    onClick = { onSelectKey(key) }
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(extended.navCapsule)
                .border(1.dp, extended.navBorder, CircleShape)
                .clickable(onClick = onAddClick)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New entry",
                tint = extended.navPlus,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AppNavigationBarPreview() {
    AppTheme(darkTheme = true) {
        AppNavigationBar(
            selectedKey = Route.Main.Journal,
            onSelectKey = {},
            onAddClick = {}
        )
    }
}
