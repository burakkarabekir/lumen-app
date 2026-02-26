package com.bksd.core.design_system.component.divider

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme

@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        AppDivider()
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppTheme(darkTheme = true) {
        AppDivider()
    }
}