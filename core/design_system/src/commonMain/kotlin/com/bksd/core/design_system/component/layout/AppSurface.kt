package com.bksd.core.design_system.component.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme

private val DefaultContentPadding = PaddingValues(vertical = 8.dp)

@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    enableScrolling: Boolean = false,
    centered: Boolean = false,
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            header()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .then(
                        if (enableScrolling) {
                            Modifier.verticalScroll(rememberScrollState())
                        } else Modifier
                    )
                    .padding(DefaultContentPadding),
                horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        AppSurface(
            content = {
                Text(text = "Test")
            }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AppSurface(
            header = {
                AppTopBar(
                    title = "Journal",
                    style = AppBarStyle.Root,
                )
            },
            content = {
                Text(text = "Test")
            }
        )
    }
}