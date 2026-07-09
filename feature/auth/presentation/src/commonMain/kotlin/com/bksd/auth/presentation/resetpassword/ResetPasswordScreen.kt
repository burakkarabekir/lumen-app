package com.bksd.auth.presentation.resetpassword

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.auth.presentation.Res
import com.bksd.auth.presentation.back_to_sign_in
import com.bksd.auth.presentation.btn_send_link
import com.bksd.auth.presentation.components.AuthFooterLink
import com.bksd.auth.presentation.components.AuthScreenScaffold
import com.bksd.auth.presentation.components.AuthTextField
import com.bksd.auth.presentation.error_invalid_email
import com.bksd.auth.presentation.label_email
import com.bksd.auth.presentation.reset_password_description
import com.bksd.auth.presentation.reset_password_title
import com.bksd.auth.presentation.reset_success_message
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordRoot(
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ResetPasswordViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ResetPasswordEvent.NavigateToSignIn -> onNavigateToSignIn()
            is ResetPasswordEvent.ResetPasswordSuccess -> Unit
            is ResetPasswordEvent.ResetPasswordError -> Unit
        }
    }

    ResetPasswordScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
internal fun ResetPasswordScreen(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
    modifier: Modifier = Modifier
) {
    AuthScreenScaffold(
        title = stringResource(Res.string.reset_password_title),
        subtitle = stringResource(Res.string.reset_password_description),
        modifier = modifier,
        footer = {
            AuthFooterLink(
                linkText = stringResource(Res.string.back_to_sign_in),
                onClick = { onAction(ResetPasswordAction.OnBackToSignInClick) },
            )
        },
    ) {
        AuthTextField(
            value = state.email,
            onValueChange = { onAction(ResetPasswordAction.OnEmailChange(it)) },
            label = stringResource(Res.string.label_email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            isError = state.emailError,
            supportingText = if (state.emailError) stringResource(Res.string.error_invalid_email) else null,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xl))

        if (state.error != null) {
            Text(
                text = state.error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = MaterialTheme.dimens.spacing.md)
            )
        }

        if (state.isSuccess) {
            Text(
                text = stringResource(Res.string.reset_success_message),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = MaterialTheme.dimens.spacing.md)
            )
        }

        AppButton(
            text = stringResource(Res.string.btn_send_link),
            onClick = { onAction(ResetPasswordAction.OnSubmitClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.size.fab),
            enabled = state.isSubmitEnabled,
            isLoading = state.isLoading,
            style = AppButtonStyle.PRIMARY
        )
    }
}

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    AppTheme {
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun ResetPasswordScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {}
        )
    }
}
