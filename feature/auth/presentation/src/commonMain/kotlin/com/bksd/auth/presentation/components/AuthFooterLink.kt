package com.bksd.auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens

@Composable
fun AuthFooterLink(
    linkText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    prompt: String? = null,
) {
    Row(
        modifier = modifier.padding(bottom = MaterialTheme.dimens.spacing.xl),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (prompt != null) {
            Text(
                text = prompt,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
        }
        Text(
            text = linkText,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable(onClick = onClick),
        )
    }
}

@Preview
@Composable
private fun AuthFooterLinkPreview() {
    AppTheme(darkTheme = true) {
        AuthFooterLink(
            prompt = "Don't have an account?",
            linkText = " Sign up",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun AuthFooterLinkNoPromptPreview() {
    AppTheme(darkTheme = true) {
        AuthFooterLink(
            linkText = "Back to Sign In",
            onClick = {},
        )
    }
}
