package com.bksd.core.design_system.component.layout

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme

@Composable
fun AppScaffold(
    snackbarHostState: SnackbarHostState? = null,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        contentWindowInsets = WindowInsets.statusBars
            .union(WindowInsets.displayCutout)
            .union(WindowInsets.ime),
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                )

            }
        }
    ) { _ ->
        content()
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        AppScaffold(
            content = {
                AppSurface {
                    Text(
                        text = "App Scaffold",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AppScaffold(
            content = {
                Text(
                    text = "App Scaffold",
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }
}