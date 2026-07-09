package com.bksd.lumen.consent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.coverGradient
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.domain.legal.LegalConfig
import com.bksd.lumen.Res
import com.bksd.lumen.consent_and
import com.bksd.lumen.consent_gate_body_prefix
import com.bksd.lumen.consent_gate_button
import com.bksd.lumen.consent_gate_title
import com.bksd.lumen.consent_privacy
import com.bksd.lumen.consent_terms
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConsentGateScreen(
    isLoading: Boolean,
    onAgree: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        ),
    )
    val prefix = stringResource(Res.string.consent_gate_body_prefix)
    val termsLabel = stringResource(Res.string.consent_terms)
    val and = stringResource(Res.string.consent_and)
    val privacyLabel = stringResource(Res.string.consent_privacy)

    val body = buildAnnotatedString {
        append(prefix)
        withLink(
            LinkAnnotation.Clickable("terms", linkStyles) {
                runCatching { uriHandler.openUri(LegalConfig.TERMS_URL) }
            }
        ) { append(termsLabel) }
        append(and)
        withLink(
            LinkAnnotation.Clickable("privacy", linkStyles) {
                runCatching { uriHandler.openUri(LegalConfig.PRIVACY_URL) }
            }
        ) { append(privacyLabel) }
        append(".")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.size.topBar)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xxl))
                .background(Brush.linearGradient(MaterialTheme.colorScheme.extended.coverGradient)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.avatar),
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xl))

        Text(
            text = stringResource(Res.string.consent_gate_title),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xxxl))

        AppButton(
            text = stringResource(Res.string.consent_gate_button),
            onClick = onAgree,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.fab),
            enabled = !isLoading,
            isLoading = isLoading,
            style = AppButtonStyle.PRIMARY,
        )
    }
}

@Preview
@Composable
private fun ConsentGateScreenPreview() {
    AppTheme(darkTheme = true) {
        ConsentGateScreen(isLoading = false, onAgree = {})
    }
}
