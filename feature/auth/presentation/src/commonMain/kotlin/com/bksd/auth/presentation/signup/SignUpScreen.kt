package com.bksd.auth.presentation.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.already_have_account
import com.bksd.auth.presentation.btn_sign_up
import com.bksd.auth.presentation.check_your_email
import com.bksd.auth.presentation.components.AuthFooterLink
import com.bksd.auth.presentation.components.AuthScreenScaffold
import com.bksd.auth.presentation.components.AuthTermsCheckbox
import com.bksd.auth.presentation.components.AuthTextField
import com.bksd.auth.presentation.confirmation_link_sent
import com.bksd.auth.presentation.create_your_space
import com.bksd.auth.presentation.error_invalid_email
import com.bksd.auth.presentation.label_email
import com.bksd.auth.presentation.label_full_name
import com.bksd.auth.presentation.label_password
import com.bksd.auth.presentation.password_hint
import com.bksd.auth.presentation.sign_in_link
import com.bksd.auth.presentation.sign_up_subtitle
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.domain.legal.LegalConfig
import com.bksd.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SignUpViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SignUpEvent.SignUpSuccess -> onNavigateToHome()
            is SignUpEvent.NavigateToSignIn -> onNavigateToSignIn()
            is SignUpEvent.SignUpError -> Unit
            is SignUpEvent.OpenPrivacyPolicy -> runCatching { uriHandler.openUri(LegalConfig.PRIVACY_URL) }
            is SignUpEvent.OpenTermsOfService -> runCatching { uriHandler.openUri(LegalConfig.TERMS_URL) }
        }
    }

    SignUpScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
internal fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.awaitingConfirmation) {
        SignUpConfirmationPanel(
            email = state.email,
            onSignInClick = { onAction(SignUpAction.OnSignInClick) },
            modifier = modifier
        )
        return
    }

    AuthScreenScaffold(
        title = stringResource(Res.string.create_your_space),
        subtitle = stringResource(Res.string.sign_up_subtitle),
        modifier = modifier,
        footer = {
            AuthFooterLink(
                prompt = stringResource(Res.string.already_have_account),
                linkText = stringResource(Res.string.sign_in_link),
                onClick = { onAction(SignUpAction.OnSignInClick) },
            )
        },
    ) {
        AuthTextField(
            value = state.fullName,
            onValueChange = { onAction(SignUpAction.OnFullNameChange(it)) },
            label = stringResource(Res.string.label_full_name),
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

        AuthTextField(
            value = state.email,
            onValueChange = { onAction(SignUpAction.OnEmailChange(it)) },
            label = stringResource(Res.string.label_email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            isError = state.emailError,
            supportingText = if (state.emailError) stringResource(Res.string.error_invalid_email) else null,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

        AuthTextField(
            value = state.password,
            onValueChange = { onAction(SignUpAction.OnPasswordChange(it)) },
            label = stringResource(Res.string.label_password),
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            supportingText = stringResource(Res.string.password_hint),
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.lg))

        AuthTermsCheckbox(
            checked = state.agreedToTerms,
            onCheckedChange = { onAction(SignUpAction.OnTermsToggle(it)) },
            onTermsClick = { onAction(SignUpAction.OnTermsClick) },
            onPrivacyClick = { onAction(SignUpAction.OnPrivacyClick) },
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

        if (state.error != null) {
            Text(
                text = state.error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = MaterialTheme.dimens.spacing.md)
            )
        }

        AppButton(
            text = stringResource(Res.string.btn_sign_up),
            onClick = { onAction(SignUpAction.OnSignUpClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.fab),
            enabled = state.isSubmitEnabled,
            isLoading = state.isLoading,
            style = AppButtonStyle.PRIMARY
        )
    }
}

@Composable
private fun SignUpConfirmationPanel(
    email: String,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AuthScreenScaffold(
        title = stringResource(Res.string.check_your_email),
        subtitle = stringResource(Res.string.confirmation_link_sent, email),
        modifier = modifier,
    ) {
        AppButton(
            text = stringResource(Res.string.sign_in_link),
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.fab),
            style = AppButtonStyle.PRIMARY
        )
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    AppTheme {
        SignUpScreen(
            state = SignUpState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun SignUpScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        SignUpScreen(
            state = SignUpState(),
            onAction = {}
        )
    }
}
