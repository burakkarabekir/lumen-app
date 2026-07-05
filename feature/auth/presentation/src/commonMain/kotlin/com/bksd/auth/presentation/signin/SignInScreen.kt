package com.bksd.auth.presentation.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.btn_sign_in
import com.bksd.auth.presentation.components.AuthFooterLink
import com.bksd.auth.presentation.components.AuthScreenScaffold
import com.bksd.auth.presentation.components.AuthTextField
import com.bksd.auth.presentation.continue_with_google
import com.bksd.auth.presentation.error_invalid_email
import com.bksd.auth.presentation.forgot_password
import com.bksd.auth.presentation.label_email
import com.bksd.auth.presentation.label_password
import com.bksd.auth.presentation.no_account_prompt
import com.bksd.auth.presentation.remember_me
import com.bksd.auth.presentation.sign_in_subtitle
import com.bksd.auth.presentation.sign_up_link
import com.bksd.auth.presentation.signin.components.ContinueWithAppleButton
import com.bksd.auth.presentation.signin.components.SocialLoginDivider
import com.bksd.auth.presentation.welcome_back
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.presentation.auth.AppleSignInResult
import com.bksd.core.presentation.auth.GoogleSignInResult
import com.bksd.core.presentation.auth.rememberAppleSignInLauncher
import com.bksd.core.presentation.auth.rememberGoogleSignInLauncher
import com.bksd.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInRoot(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SignInViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SignInEvent.SignInSuccess -> onNavigateToHome()
            is SignInEvent.NavigateToSignUp -> onNavigateToSignUp()
            is SignInEvent.NavigateToForgotPassword -> onNavigateToForgotPassword()
            is SignInEvent.SignInError -> Unit
        }
    }

    SignInScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
internal fun SignInScreen(
    state: SignInState,
    onAction: (SignInAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val googleLauncher = rememberGoogleSignInLauncher { result ->
        when (result) {
            is GoogleSignInResult.Success ->
                onAction(SignInAction.OnGoogleIdTokenReceived(result.idToken))

            GoogleSignInResult.Cancelled ->
                onAction(SignInAction.OnGoogleSignInFailed(cancelled = true))

            is GoogleSignInResult.Failed ->
                onAction(SignInAction.OnGoogleSignInFailed(cancelled = false))
        }
    }

    val appleLauncher = rememberAppleSignInLauncher { result ->
        when (result) {
            is AppleSignInResult.Success ->
                onAction(SignInAction.OnAppleIdTokenReceived(result.idToken, result.nonce))

            AppleSignInResult.Cancelled ->
                onAction(SignInAction.OnAppleSignInFailed(cancelled = true))

            is AppleSignInResult.Failed ->
                onAction(SignInAction.OnAppleSignInFailed(cancelled = false))
        }
    }

    AuthScreenScaffold(
        title = stringResource(Res.string.welcome_back),
        subtitle = stringResource(Res.string.sign_in_subtitle),
        modifier = modifier,
        footer = {
            AuthFooterLink(
                prompt = stringResource(Res.string.no_account_prompt),
                linkText = stringResource(Res.string.sign_up_link),
                onClick = { onAction(SignInAction.OnSignUpClick) },
            )
        },
    ) {
        AuthTextField(
            value = state.email,
            onValueChange = { onAction(SignInAction.OnEmailChange(it)) },
            label = stringResource(Res.string.label_email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            isError = state.emailError,
            supportingText = if (state.emailError) stringResource(Res.string.error_invalid_email) else null,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

        AuthTextField(
            value = state.password,
            onValueChange = { onAction(SignInAction.OnPasswordChange(it)) },
            label = stringResource(Res.string.label_password),
            isPassword = true,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = state.rememberMe,
                    onCheckedChange = { onAction(SignInAction.OnRememberMeToggle(it)) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline,
                    ),
                )
                Text(
                    text = stringResource(Res.string.remember_me),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
            }
            TextButton(
                onClick = { onAction(SignInAction.OnForgotPasswordClick) }
            ) {
                Text(
                    text = stringResource(Res.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xl))

        if (state.error != null) {
            Text(
                text = state.error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = MaterialTheme.dimens.spacing.md)
            )
        }

        AppButton(
            text = stringResource(Res.string.btn_sign_in),
            onClick = { onAction(SignInAction.OnSignInClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.fab),
            enabled = state.isSubmitEnabled,
            isLoading = state.isLoading,
            style = AppButtonStyle.PRIMARY
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.lg))

        SocialLoginDivider()

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.lg))

        AppButton(
            text = stringResource(Res.string.continue_with_google),
            onClick = { googleLauncher.launch() },
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.fab),
            enabled = !state.isLoading && !state.isSocialLoading,
            isLoading = state.isSocialLoading,
            style = AppButtonStyle.SECONDARY
        )

        if (appleLauncher != null) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.md))

            ContinueWithAppleButton(
                onClick = { appleLauncher.launch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.size.fab),
                enabled = !state.isLoading && !state.isSocialLoading,
                isLoading = state.isSocialLoading,
            )
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    AppTheme {
        SignInScreen(
            state = SignInState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun SignInScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        SignInScreen(
            state = SignInState(),
            onAction = {}
        )
    }
}
