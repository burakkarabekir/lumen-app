package com.bksd.core.design_system.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.Res
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.navigate_back
import com.bksd.core.design_system.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

sealed class AppBarStyle {
    data object Root : AppBarStyle()
    data object Child : AppBarStyle()
}

@Composable
fun AppTopBar(
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    style: AppBarStyle= AppBarStyle.Root,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = {},
    actions: (@Composable RowScope.() -> Unit)? = {},
    showDivider: Boolean = true,

    ) {
    val titleTextAlign = when (style) {
        AppBarStyle.Root -> TextAlign.Start
        AppBarStyle.Child -> TextAlign.Center
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
    ) {
            Row(
                modifier = Modifier
                    .height(72.dp)
                    .then(
                        if (titleTextAlign == TextAlign.Start) {
                            Modifier
                                .padding(
                                    top = 16.dp,
                                    start = 12.dp,
                                    end = 12.dp,
                                    bottom = 8.dp
                                )
                        } else {
                            Modifier
                        }
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (style) {
                    AppBarStyle.Root -> Unit // no nav icon
                    AppBarStyle.Child -> navigationIcon?.invoke()
                }
                if (title != null) {
                    Text(
                        text = title,
                        style = when (style) {
                            AppBarStyle.Root -> MaterialTheme.typography.headlineLarge
                            AppBarStyle.Child -> MaterialTheme.typography.titleMedium
                        },
                        textAlign = titleTextAlign,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                    )
                    if (titleTextAlign == TextAlign.Center) {
                        Spacer(modifier = Modifier.width(32.dp))
                    }
                } else {
                    titleContent?.invoke()
                }

                actions?.let {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        content = it
                    )
                }
            }
        if (showDivider) AppDivider()
    }
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        AppTopBar(
            title = "News List",
            style = AppBarStyle.Root
        )
    }
}

@Preview
@Composable
private fun PreviewWithBack() {
    AppTheme {
        AppTopBar(
            title = "News List",
            style = AppBarStyle.Child
        )
    }
}

@Preview
@Composable
private fun PreviewWithChildBack() {
    AppTheme {
        AppTopBar(
            title = "News List",
            style = AppBarStyle.Child,
            navigationIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(Res.string.navigate_back)
                    )
                }
            }
        )
    }
}