package com.bksd.auth.presentation.signin.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.or_continue_with
import com.bksd.core.design_system.component.divider.AppDivider
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.LumenBase600
import com.bksd.core.design_system.theme.LumenSpacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun SocialLoginDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppDivider( modifier = Modifier.weight(1f))
        Text(
            text = stringResource(Res.string.or_continue_with),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = LumenSpacing.md)
        )
        AppDivider( modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun SocialLoginDividerPreview() {
    AppTheme(darkTheme = true) {
        SocialLoginDivider()
    }
}
