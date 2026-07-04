package com.bksd.auth.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.privacy_policy
import com.bksd.auth.presentation.terms_and
import com.bksd.auth.presentation.terms_checkbox_prefix
import com.bksd.auth.presentation.terms_of_service
import com.bksd.core.design_system.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthTermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        ),
    )
    val prefix = stringResource(Res.string.terms_checkbox_prefix)
    val termsLabel = stringResource(Res.string.terms_of_service)
    val and = stringResource(Res.string.terms_and)
    val privacyLabel = stringResource(Res.string.privacy_policy)

    val agreementText = buildAnnotatedString {
        append(prefix)
        withLink(LinkAnnotation.Clickable("terms", linkStyles) { onTermsClick() }) {
            append(termsLabel)
        }
        append(and)
        withLink(LinkAnnotation.Clickable("privacy", linkStyles) { onPrivacyClick() }) {
            append(privacyLabel)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline,
            ),
        )
        Text(
            text = agreementText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
    }
}

@Preview
@Composable
private fun AuthTermsCheckboxPreview() {
    AppTheme(darkTheme = true) {
        AuthTermsCheckbox(
            checked = true,
            onCheckedChange = {},
            onTermsClick = {},
            onPrivacyClick = {},
        )
    }
}
